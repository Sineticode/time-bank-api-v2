package fi.metatavu.timebank.api.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import fi.metatavu.timebank.api.persistence.repositories.TimeEntryRepository
import fi.metatavu.timebank.model.PersonTotalTime
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
    suspend fun getPersonTotal(personId: Int): PersonTotalTime? {
        return calculatePersonTotalTime(personId)
    }

    /**
     * List persons data from Forecast API
     *
     * @return List of forecastPersons
     */
    suspend fun getPersonsFromForecast(): List<ForecastPerson> {
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
    suspend fun filterActivePersons(persons: List<ForecastPerson>): List<ForecastPerson> {
        return persons.filter{ i -> i.active && !i.is_system_user }
    }

    /**
     * Lists Forecast persons based on optional query parameters
     *
     * @param active
     * @return List of Forecast persons
     */
    suspend fun listPersons(active: Boolean?): List<ForecastPerson> {
        var persons = getPersonsFromForecast()
        if (active == true) persons = filterActivePersons(persons)
        return persons
    }
    /**
     * Calculates persons combined total time
     *
     * @param personId personId
     * @return PersonTotalTime
     */
    private suspend fun calculatePersonTotalTime(personId: Int): PersonTotalTime {
        val personEntries = timeEntryRepository.getEntriesById(personId)
        var logged = 0
        var expected = 0
        var internalTime = 0
        var projectTime = 0
        personEntries.map{ entry ->
            logged += entry.internalTime?: 0
            logged += entry.projectTime?: 0
            internalTime += entry.internalTime?: 0
            projectTime += entry.projectTime?: 0
            expected += 435
        }
        val balance = expected - logged
        return PersonTotalTime(
            balance,
            logged,
            expected,
            internalTime,
            projectTime,
            personId
        )
    }
}