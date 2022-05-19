package fi.metatavu.timebank.api.controllers

import com.google.gson.Gson
import fi.metatavu.timebank.api.persistence.model.DailyEntry
import fi.metatavu.timebank.api.persistence.model.Person
import fi.metatavu.timebank.api.persistence.repositories.DailyEntryRepository
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
    lateinit var dailyEntryRepository: DailyEntryRepository

    @Inject
    lateinit var forecastService: ForecastService

    @Inject
    lateinit var logger: Logger

    val gson: Gson = Gson()

    /**
     * Lists persons total time
     *
     * @param personId personId
     * @return List of dailyEntries
     */
    suspend fun getPersonTotal(personId: Int): List<DailyEntry> {
        return dailyEntryRepository.getEntriesById(personId)
    }

    /**
     * List persons data from Forecast API
     *
     * @return Array of forecastPersons
     */
    suspend fun getPersonsFromForecast(): Array<ForecastPerson> {
        return try {
            val resultString = forecastService.getPersons()
            gson.fromJson(resultString, Array<ForecastPerson>::class.java)
        } catch (e: Error) {
            logger.error("Error when executing get request: ${e.localizedMessage}")
            throw Error(e.localizedMessage)
        }
    }

    /**
     * Filters inactive persons and system users
     *
     * @return List of persons
     */
    suspend fun filterActivePersons(persons: List<Person>): List<Person> {
        return persons.filter{ person -> person.active!! && person.defaultRole != null}
    }
}