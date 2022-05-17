package fi.metatavu.timebank.api.controllers

import com.google.gson.Gson
import fi.metatavu.timebank.api.persistence.model.DailyEntry
import fi.metatavu.timebank.api.persistence.model.Person
import fi.metatavu.timebank.api.persistence.repositories.DailyEntryRepository
import okhttp3.OkHttpClient
import okhttp3.Request
import org.eclipse.microprofile.config.ConfigProvider
import java.net.URL
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject


@ApplicationScoped
class PersonsController {

    @Inject
    lateinit var dailyEntryRepository: DailyEntryRepository

    val gson: Gson = Gson()

    suspend fun getPersonTotal(personId: Int): List<DailyEntry> {
        return dailyEntryRepository.getEntriesById(personId)
    }

    fun getPersonsFromForecast(active: Boolean?): Any? {
        val forecastBaseUrl = ConfigProvider.getConfig().getValue(
            "forecast.base.url",
            String::class.java
        )
        val forecastApiKey = ConfigProvider.getConfig().getValue(
            "forecast.api.key",
            String::class.java
        )
        val url: URL = URL("${forecastBaseUrl}persons")
        var client: OkHttpClient = OkHttpClient()
        var result: Any? = ""
        try {
            val request =
                Request.Builder().url(url).addHeader("X-FORECAST-API-KEY", forecastApiKey)
                    .build()
            val response = client.newCall(request).execute()
            result = response.body()?.string()
            val personsArray: Array<Person> = gson.fromJson(result, Array<Person>::class.java)
            result = personsArray.filter{ i -> i.active}
            when (response.code()) {
                200 -> return result
                404 -> throw Error("Bad request")
            }
        } catch (e: Error) {
            println("Error when executing get request: ${e.localizedMessage}")
            return e.localizedMessage
        }
        return result
    }

    private fun filterActivePersons(persons: List<Person>): List<Any> {
        return persons.filter{ i -> i.active }
    }
}