package fi.metatavu.timebank.api.test.functional.tests

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import fi.metatavu.timebank.api.test.functional.TestBuilder
import io.quarkus.test.common.DevServicesContext
import okhttp3.*
import org.eclipse.microprofile.config.ConfigProvider
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.slf4j.Logger
import javax.inject.Inject

/**
 * Abstract Test class
 */
abstract class AbstractTest {

    @Inject
    lateinit var logger: Logger

    @ConfigProperty(name = "forecast.base.url")
    lateinit var forecastBaseUrl: String

    private var devServicesContext: DevServicesContext? = null

    protected fun createTestBuilder(): TestBuilder {
        return TestBuilder(getConfig())
    }

    private fun getConfig(): Map<String, String> {
        return getDevServiceConfig() ?: getQuarkusConfig()
    }

    private fun getDevServiceConfig(): Map<String, String>? {
        return devServicesContext?.devServicesProperties()
    }

    private fun getQuarkusConfig(): Map<String, String> {
        val config = ConfigProvider.getConfig()
        return config.propertyNames.associateWith { config.getConfigValue(it).rawValue }
    }

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
            logger.error("Un-expected error happened while resetting Wiremock Scenarios: ${e.localizedMessage}")
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
            logger.error("Un-expected error happened while setting Wiremock Scenarios: ${e.localizedMessage}")
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

data class ReqBody(
    val state: String
)