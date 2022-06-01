package fi.metatavu.timebank.api.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import fi.metatavu.timebank.api.forecast.ForecastService
import fi.metatavu.timebank.api.forecast.models.ForecastTimeEntry
import fi.metatavu.timebank.api.impl.translate.TimeEntryTranslator
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
    lateinit var timeEntryTranslator: TimeEntryTranslator

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
        val forecastPersons = personsController.getPersonsFromForecast() ?: return null
        val persons = personsTranslator.translate(personsController.filterActivePersons(forecastPersons))
        val resultString = forecastService.getTimeEntries(after) ?: return null
        val forecastTimeEntries = jacksonObjectMapper().readValue(resultString, Array<ForecastTimeEntry>::class.java).toList()
        val translatedEntries = timeEntryTranslator.translate(forecastTimeEntries)
        var synchronized = 0
        translatedEntries.forEachIndexed { idx, timeEntry ->
            val personName =
                persons.find { person -> person.id == timeEntry.person}?.lastName + ", " + persons.find { person -> person.id == timeEntry.person}?.firstName
            logger.info("Synchronizing TimeEntry ${idx + 1}/${translatedEntries.size} of $personName...")
            if (timeEntryRepository.persistEntry(timeEntry)) {
                synchronized++
                logger.info("Synchronized TimeEntry #${synchronized}!")
            }
            else logger.warn("Time Entry ${idx + 1}/${translatedEntries.size} already synchronized!")
        }
        return synchronized
    }

}