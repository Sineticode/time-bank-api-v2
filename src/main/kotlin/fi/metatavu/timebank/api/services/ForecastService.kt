package fi.metatavu.timebank.api.services

import okhttp3.OkHttpClient
import okhttp3.Request
import org.eclipse.microprofile.config.ConfigProvider
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ForecastService {
    private val forecastBaseUrl = ConfigProvider.getConfig().getValue(
        "forecast.base.url",
        String::class.java
    )

    private val forecastApiKey = ConfigProvider.getConfig().getValue(
        "forecast.api.key",
        String::class.java
    )

    private var client: OkHttpClient = OkHttpClient()

    /**
     * Sends get request to Forecast API
     *
     * @param path path for the request
     * @return Response from the request
     */
    fun doRequest(path: String): String? {
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

    /**
     * Gets time registrations from Forecast
     *
     * @OptionalParam date after in YYYYMMDD string format
     * @return Response with time registrations data
     */
    fun getTimeEntries(after: String?): String? {
        var path = "/v3/time_registrations"
        if (after != null) path += "?date_after=$after"
        return doRequest(path)
    }

}