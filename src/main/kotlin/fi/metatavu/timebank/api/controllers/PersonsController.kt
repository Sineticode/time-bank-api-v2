package fi.metatavu.timebank.api.controllers

import com.google.gson.Gson
import fi.metatavu.timebank.api.impl.translate.ForecastPerson
import fi.metatavu.timebank.api.persistence.model.DailyEntry
import fi.metatavu.timebank.api.persistence.model.Person
import fi.metatavu.timebank.api.persistence.repositories.DailyEntryRepository
import fi.metatavu.timebank.api.impl.translate.PersonsTranslator
import fi.metatavu.timebank.api.services.ForecastService
import fi.metatavu.timebank.model.PersonTotalTime
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject


@ApplicationScoped
class PersonsController {

    @Inject
    lateinit var dailyEntryRepository: DailyEntryRepository

    @Inject
    lateinit var personsTranslator: PersonsTranslator

    @Inject
    lateinit var forecastService: ForecastService

    val gson: Gson = Gson()

    suspend fun getPersonTotal(personId: Int): PersonTotalTime? {
        return calculatePersonTotalTime(personId)
    }

    suspend fun getPersonsFromForecast(active: Boolean?): List<Person> {
        var result: List<Person>
        try {
            val resultString = forecastService.getPersons()
            val forecastPersonsArray: Array<ForecastPerson> = gson.fromJson(resultString, Array<ForecastPerson>::class.java)
            result = personsTranslator.translatePersons(forecastPersonsArray)
            if (active == true) result = filterActivePersons(result)
        } catch (e: Error) {
            println("Error when executing get request: ${e.localizedMessage}")
            throw Error(e.localizedMessage)
        }
        return result
    }
    private fun filterActivePersons(persons: List<Person>): List<Person> {
        return persons.filter{ i -> i.active!! && i.defaultRole != null }
    }

    private suspend fun calculatePersonTotalTime(personId: Int): PersonTotalTime {
        val personEntries: List<DailyEntry> = dailyEntryRepository.getEntriesById(personId)
        var logged = 0
        var expected = 0
        var internalTime = 0
        var projectTime = 0
        personEntries.map{ i ->
            logged += i.logged
            expected += i.expected
            internalTime += i.internalTime
            projectTime += i.projectTime
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