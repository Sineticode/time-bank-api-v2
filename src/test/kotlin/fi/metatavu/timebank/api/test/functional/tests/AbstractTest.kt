package fi.metatavu.timebank.api.test.functional.tests

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.*
import org.eclipse.microprofile.config.ConfigProvider

/**
 * Abstract Test class
 */
abstract class AbstractTest {

    private var forecastBaseUrl = ConfigProvider.getConfig().getValue("forecast.base.url", String::class.java)

    data class ReqBody(val state: String)

    /**
     * Resets Wiremock scenario states
     */
    fun resetScenarios() {
        try {
            val client = OkHttpClient()
            client
                .newCall(
                    Request.Builder()
                    .url("$forecastBaseUrl/__admin/scenarios/reset")
                    .post(RequestBody.create(null, ""))
                    .build()
                ).execute()
                .close()
        } catch (e: Error) {
            println("Un-expected error happened while resetting Wiremock Scenarios: ${e.localizedMessage}")
        }
    }

    /**
     * Sets specific Wiremock scenario to specific state
     */
    fun setScenario(scenario: String, state: String) {
        try {
            val client = OkHttpClient()
            client
                .newCall(
                    Request.Builder()
                        .url("$forecastBaseUrl/__admin/scenarios/$scenario/state")
                        .put(RequestBody.create(
                            MediaType.parse("application/json"),
                            jacksonObjectMapper().writeValueAsString(ReqBody(state = state)
                            )
                        ))
                        .build()
                ).execute()
                .close()
        } catch (e: Error) {
            println("Un-expected error happened while setting Wiremock Scenarios: ${e.localizedMessage}")
        }
    }

    companion object {
        const val PERSONS_SCENARIO = "personsScenario"
        const val HOLIDAYS_SCENARIO = "holidaysScenario"
        const val TIMES_SCENARIO = "timesScenario"
        const val ERROR_STATE = "errorState"
        const val UPDATE_STATE = "updateState"
    }
}