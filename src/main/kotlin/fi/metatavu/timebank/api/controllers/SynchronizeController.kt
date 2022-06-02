package fi.metatavu.timebank.api.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import fi.metatavu.timebank.api.forecast.ForecastService
import fi.metatavu.timebank.api.forecast.models.ForecastTimeEntry
import fi.metatavu.timebank.api.forecast.translate.ForecastTimeEntryTranslator
import fi.metatavu.timebank.api.impl.translate.PersonsTranslator
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
    lateinit var personsController: PersonsController

    @Inject
    lateinit var timeEntryRepository: TimeEntryRepository

    @Inject
    lateinit var forecastService: ForecastService

    @Inject
    lateinit var forecastTimeEntryTranslator: ForecastTimeEntryTranslator

    @Inject
    lateinit var personsTranslator: PersonsTranslator

    @Inject
    lateinit var logger: Logger

    /**
     * Synchronizes time entries from Forecast to Time-bank
     *
     * @param after YYYY-MM-DD LocalDate
     * @return Length of entries synchronized
     */
    suspend fun synchronize(after: LocalDate?): Int? {
        return try {
            val forecastPersons = personsController.getPersonsFromForecast()
            val resultString = forecastService.getTimeEntries(after)

            if (forecastPersons.isNullOrEmpty() || resultString.isNullOrEmpty()) {
                return null
            }

            val persons = personsTranslator.translate(personsController.filterActivePersons(forecastPersons))
            val forecastTimeEntries = jacksonObjectMapper().readValue(resultString, Array<ForecastTimeEntry>::class.java).toList()
            val translatedEntries = forecastTimeEntryTranslator.translate(forecastTimeEntries)

            var synchronized = 0

            translatedEntries.forEachIndexed { idx, timeEntry ->
                val person = persons.find { person -> person.id == timeEntry.person }
                val personName = "${person?.lastName}, ${person?.firstName}"
                logger.info("Synchronizing TimeEntry ${idx + 1}/${translatedEntries.size} of $personName...")

                if (timeEntryRepository.persistEntry(timeEntry)) {
                    synchronized++
                    logger.info("Synchronized TimeEntry #${synchronized}!")
                } else {
                    logger.warn("Time Entry ${idx + 1}/${translatedEntries.size} already synchronized!")
                }
            }

            synchronized
        } catch (e: Error) {
            logger.error(e.localizedMessage)
            null
        }
    }

}