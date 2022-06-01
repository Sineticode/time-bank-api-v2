package fi.metatavu.timebank.api.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import fi.metatavu.timebank.api.persistence.model.TimeEntry
import fi.metatavu.timebank.api.persistence.repositories.TimeEntryRepository
import fi.metatavu.timebank.api.forecast.ForecastService
import fi.metatavu.timebank.api.forecast.models.ForecastPerson
import org.slf4j.Logger
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

/**
 * Controller for Person objects
 */
@ApplicationScoped
class PersonsController {

    @Inject
    lateinit var timeEntryRepository: TimeEntryRepository

    @Inject
    lateinit var forecastService: ForecastService

    @Inject
    lateinit var logger: Logger

    /**
     * Lists persons total time
     *
     * @param personId personId
     * @return List of dailyEntries
     */
    suspend fun getPersonTotal(personId: Int): List<TimeEntry> {
        return timeEntryRepository.getEntriesByPersonId(personId)
    }

    /**
     * List persons data from Forecast API
     *
     * @return Array of forecastPersons
     */
    private fun getPersonsFromForecast(): List<ForecastPerson> {
        return try {
            val resultString = forecastService.getPersons()
            jacksonObjectMapper().readValue(resultString, Array<ForecastPerson>::class.java).toList()
        } catch (e: Error) {
            logger.error("Error when executing get request: ${e.localizedMessage}")
            throw Error(e.localizedMessage)
        }
    }

    /**
     * Filters inactive Forecast persons and system users
     *
     * @return List of Forecast persons
     */
    private fun filterActivePersons(persons: List<ForecastPerson>): List<ForecastPerson> {
        return persons.filter{ person -> person.active && !person.is_system_user }
    }

    /**
     * Lists Forecast persons based on optional query parameters
     *
     * @param active
     * @return List of Forecast persons
     */
    suspend fun listPersons(active: Boolean?): List<ForecastPerson> {
        val persons = getPersonsFromForecast()

        return if (active == true) {
            filterActivePersons(persons)
        } else {
            persons
        }
    }
}