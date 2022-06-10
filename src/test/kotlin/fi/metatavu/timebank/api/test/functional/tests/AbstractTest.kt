package fi.metatavu.timebank.api.test.functional.tests

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.eclipse.microprofile.config.inject.ConfigProperty

/**
 * Abstract Test class
 */
abstract class AbstractTest {

    @ConfigProperty(name = "forecast.base.url")
    lateinit var forecastBaseUrl: String

    /**
     * Resets Wiremock scenario states
     */
    fun resetScenarios() {
        try {
            val client = OkHttpClient()
            client
                .newCall(
                    Request.Builder().url("$forecastBaseUrl/__admin/scenarios/reset")
                    .post(RequestBody.create(null, ""))
                    .build()
                ).execute()
                .close()
        } catch (e: Error) {
            println("Un-expected error happening while resetting Wiremock Scenarios: ${e.localizedMessage}")
        }
    }
}