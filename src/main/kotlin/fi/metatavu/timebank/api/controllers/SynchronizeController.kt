package fi.metatavu.timebank.api.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import fi.metatavu.timebank.api.forecast.ForecastService
import fi.metatavu.timebank.api.forecast.models.ForecastTimeEntryResponse
import fi.metatavu.timebank.api.forecast.translate.ForecastTimeEntryTranslator
import fi.metatavu.timebank.api.persistence.model.TimeEntry
import fi.metatavu.timebank.api.persistence.model.WorktimeCalendar
import fi.metatavu.timebank.api.persistence.repositories.TimeEntryRepository
import org.slf4j.Logger
import java.time.LocalDate
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

/**
 * Controller for synchronization Forecast time registrations with Time-bank
 */
@ApplicationScoped
class SynchronizeController {

    @Inject
    lateinit var worktimeCalendarController: WorktimeCalendarController

    @Inject
    lateinit var personsController: PersonsController

    @Inject
    lateinit var timeEntryRepository: TimeEntryRepository

    @Inject
    lateinit var forecastService: ForecastService

    @Inject
    lateinit var forecastTimeEntryTranslator: ForecastTimeEntryTranslator

    @Inject
    lateinit var logger: Logger

    /**
     * Synchronizes time entries from Forecast to Time-bank
     *
     * @param after YYYY-MM-DD LocalDate
     * @return Length of entries synchronized
     */
    suspend fun synchronize(after: LocalDate?): List<TimeEntry> {
        return try {
            val forecastPersons = personsController.getPersonsFromForecast()

            val worktimeCalendars = forecastPersons.map { person ->
                worktimeCalendarController.checkWorktimeCalendar(person)
            }

            val entries = retrieveAllEntries(after, worktimeCalendars)

            val synchronized = mutableListOf<TimeEntry>()
            var duplicates = 0

            entries.forEachIndexed { idx, timeEntry ->
                val person = forecastPersons.find { person -> person.id == timeEntry.person }
                val personName = "${person?.lastName}, ${person?.firstName}"
                logger.info("Synchronizing TimeEntry ${idx + 1}/${entries.size} of $personName...")

                if (timeEntryRepository.persistEntry(timeEntry)) {
                    synchronized.add(timeEntry)
                    logger.info("Synchronized TimeEntry #${idx + 1}!")
                } else {
                    duplicates++
                    logger.warn("Time Entry ${idx + 1}/${entries.size} already synchronized!")
                }
            }
            logger.info("Finished synchronization with: ${synchronized.size} entries synchronized... $duplicates entries NOT synchronized...")

            synchronized
        } catch (e: Error) {
            logger.error("Error during synchronization: ${e.localizedMessage}")
            throw Error(e.localizedMessage)
        }
    }

    /**
     * Loops through paginated API responses of varying sizes from Forecast API
     * and translates the received ForecastTimeEntries to TimeEntries.
     *
     * @param after YYYY-MM-DD LocalDate'
     * @return List of TimeEntries
     */
    private suspend fun retrieveAllEntries(after: LocalDate?, worktimeCalendars: List<WorktimeCalendar>): List<TimeEntry> {
        var retrievedAllEntries = false
        val translatedEntries = mutableListOf<TimeEntry>()
        var pageNumber = 1

        while (!retrievedAllEntries) {
            val resultString = forecastService.getTimeEntries(after, pageNumber)

            val forecastTimeEntryResponse = jacksonObjectMapper().readValue(resultString, ForecastTimeEntryResponse::class.java)
            val amountOfPages =
                if (forecastTimeEntryResponse.totalObjectCount / forecastTimeEntryResponse.pageSize < 1) 1
                else forecastTimeEntryResponse.totalObjectCount / forecastTimeEntryResponse.pageSize
            logger.info("Retrieved page $pageNumber/${amountOfPages} of time registrations from Forecast API!")

            if (pageNumber * forecastTimeEntryResponse.pageSize < forecastTimeEntryResponse.totalObjectCount) {
                pageNumber++
            } else {
                retrievedAllEntries = true
            }

            translatedEntries.addAll(forecastTimeEntryTranslator.translate(
                    entities = forecastTimeEntryResponse.pageContents!!,
                    worktimeCalendars = worktimeCalendars
                )
            )
        }

        return translatedEntries
    }
}