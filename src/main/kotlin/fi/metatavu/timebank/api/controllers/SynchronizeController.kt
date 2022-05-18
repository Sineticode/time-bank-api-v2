package fi.metatavu.timebank.api.controllers

import com.google.gson.Gson
import fi.metatavu.timebank.api.impl.translate.DailyEntryTranslator
import fi.metatavu.timebank.api.impl.translate.ForecastTimeEntry
import fi.metatavu.timebank.api.persistence.model.Person
import fi.metatavu.timebank.api.services.ForecastService
import java.time.LocalDate
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class SynchronizeController {

    @Inject
    lateinit var personsController: PersonsController

    @Inject
    lateinit var dailyEntryController: DailyEntryController

    @Inject
    lateinit var forecastService: ForecastService

    @Inject
    lateinit var dailyEntryTranslator: DailyEntryTranslator

    val gson: Gson = Gson()


    suspend fun synchronize(after: LocalDate?): Int {
        val persons: List<Person> = personsController.getPersonsFromForecast(active = true)
        val resultString = forecastService.getTimeEntries(after)
        val forecastTimeEntryArray: Array<ForecastTimeEntry> = gson.fromJson(resultString, Array<ForecastTimeEntry>::class.java)
        val translatedEntries = dailyEntryTranslator.translateTimeEntry(forecastTimeEntryArray)
        return dailyEntryController.dailyEntryRepository.synchronizeEntries(translatedEntries, persons)
    }

}