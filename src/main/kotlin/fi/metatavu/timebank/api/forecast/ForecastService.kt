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
            response.body()?.string()
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
    suspend fun getPersons(): String? {
        return doRequest("/v2/persons")
    }

    /**
     * Gets time registrations from Forecast
     *
     * @OptionalParam date after in YYYY-MM-DD LocalDate
     * @return Response with time registrations data
     */
    suspend fun getTimeEntries(after: LocalDate?): String? {
        var path = "/v3/time_registrations"
        if (after != null) path += "?date_after=${after.toString().replace("-", "")}"
        return doRequest(path)
    }

    /**
     * Gets holiday times from Forecast
     *
     * @return Response with holiday times data
     */
    suspend fun getHolidays(): String? {
        return doRequest("/v1/holiday_calendar_entries")
    }
}