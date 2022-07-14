package fi.metatavu.timebank.api.forecast

import okhttp3.OkHttpClient
import okhttp3.Request
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.slf4j.Logger
import java.time.LocalDate
import javax.enterprise.context.RequestScoped
import javax.inject.Inject

/**
 * Service for accessing Forecast API
 */
@RequestScoped
class ForecastService {

    @ConfigProperty(name = "forecast.base.url")
    lateinit var forecastBaseUrl: String

    @ConfigProperty(name = "forecast.api.key")
    lateinit var forecastApiKey: String

    @Inject
    lateinit var logger: Logger

    /**
     * Sends get request to Forecast API
     *
     * @param path path for the request
     * @return Response from the request
     */
    private fun doRequest(path: String): String? {
        return try {
            val client = OkHttpClient()
            val request = Request.Builder().url("${forecastBaseUrl}${path}")
                .addHeader("X-FORECAST-API-KEY", forecastApiKey)
                .build()
            val response = client.newCall(request).execute()
            when (response.code()) {
                200 -> response.body()?.string()
                else -> throw Error("Couldn't reach Forecast API.")
            }
        } catch (e: Error) {
            logger.error("Error when executing get request: ${e.localizedMessage}")
            throw Error(e.localizedMessage)
        }
    }

    /**
     * Gets persons from Forecast
     *
     * @return Response with persons' data
     */
    fun getPersons(): String? {
        return doRequest("/v2/persons")
    }

    /**
     * Gets time registrations from Forecast
     *
     * @param date after in YYYY-MM-DD LocalDate
     * @param pageNumber page of paginated response to request
     * @return Response with time registrations data
     */
    fun getTimeEntries(after: LocalDate?, pageNumber: Int): String? {
        val pathSections = mutableListOf<String>()
        pathSections.add("/v4/time_registrations")
        if (after != null) {
            pathSections.add("/updated_after/${after.toString().replace("-", "")}T000000")
        }
        pathSections.add("?pageSize=1000&pageNumber=$pageNumber")
        return doRequest(pathSections.joinToString(""))
    }

    /**
     * Gets holiday times from Forecast
     *
     * @return Response with holiday times data
     */
    fun getHolidays(): String? {
        return doRequest("/v1/holiday_calendar_entries")
    }
}