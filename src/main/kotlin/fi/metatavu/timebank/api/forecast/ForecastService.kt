package fi.metatavu.timebank.api.forecast

import okhttp3.OkHttpClient
import okhttp3.Request
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.slf4j.Logger
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
    fun getPersons(): String? {
        return doRequest("/v2/persons")
    }

}