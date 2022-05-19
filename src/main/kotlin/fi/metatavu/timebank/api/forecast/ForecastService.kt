package fi.metatavu.timebank.api.forecast

import okhttp3.OkHttpClient
import okhttp3.Request
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.enterprise.context.RequestScoped

/**
 *
 */
@RequestScoped
class ForecastService {

    @ConfigProperty(name = "forecast.base.url")
    lateinit var forecastBaseUrl: String

    @ConfigProperty(name = "forecast.api.key")
    lateinit var forecastApiKey: String


    /**
     * Sends get request to Forecast API
     *
     * @param path path for the request
     * @return Response from the request
     */
    private fun doRequest(path: String): String? {
        val client = OkHttpClient()
        val request = Request.Builder().url("${forecastBaseUrl}${path}")
            .addHeader("X-FORECAST-API-KEY", forecastApiKey)
            .build()
        val response = client.newCall(request).execute()
        return response.body()?.string()
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