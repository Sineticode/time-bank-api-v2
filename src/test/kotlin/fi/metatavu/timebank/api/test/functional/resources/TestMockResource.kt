package fi.metatavu.timebank.api.test.functional.resources

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED
import fi.metatavu.timebank.api.test.functional.tests.TestData
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager

/**
 * Wiremock to mock Forecast API
 */
class TestMockResource: QuarkusTestResourceLifecycleManager {
    private var wireMockServer = WireMockServer()
    private val objectMapper = ObjectMapper()

    override fun start(): Map<String, String> {
        objectMapper.findAndRegisterModules()
        wireMockServer = WireMockServer(8082)
        wireMockServer.start()
        configureFor("localhost", 8082)

        personsStubs(wireMockServer)
        holidayCalendarStubs(wireMockServer)
        timeRegistrationStubs(wireMockServer)

        return mapOf(Pair("forecast.base.url", wireMockServer.baseUrl()))
    }

    /**
     * /v2/persons -stubs
     */
    private fun personsStubs(wireMockServer: WireMockServer) {
        wireMockServer.stubFor(
            get(urlPathEqualTo("/v2/persons"))
                .inScenario("scenario")
                .whenScenarioStateIs(STARTED)
                .willSetStateTo("errorState")
                .willReturn(jsonResponse(objectMapper.writeValueAsString(TestData.getPersons()), 200))
        )

    wireMockServer.stubFor(
            get(urlPathEqualTo("/v2/persons"))
                .inScenario("scenario")
                .whenScenarioStateIs("errorState")
                .willSetStateTo(STARTED)
                .willReturn(
                    jsonResponse(
                        objectMapper.writeValueAsString(
                            ForecastErrorResponse(
                                status = 401,
                                message = "Server failed to authenticate the request."
                            )
                        ), 401
                    )
                )
        )
    }

    /**
     * /v1/holiday_calendar_entries -stubs
     */
    private fun holidayCalendarStubs(wireMockServer: WireMockServer) {
        wireMockServer.stubFor(
            get(urlPathEqualTo("/v1/holiday_calendar_entries"))
                .inScenario("scenarioT")
                .whenScenarioStateIs(STARTED)
                .willSetStateTo("errorState")
                .willReturn(jsonResponse(objectMapper.writeValueAsString(TestData.getHolidays()), 200))
        )
        wireMockServer.stubFor(
            get(urlPathEqualTo("/v1/holiday_calendar_entries"))
                .inScenario("scenarioT")
                .whenScenarioStateIs("errorState")
                .willSetStateTo(STARTED)
                .willReturn(
                    jsonResponse(
                        objectMapper.writeValueAsString(
                            ForecastErrorResponse(
                                status = 401,
                                message = "Server failed to authenticate the request."
                            )
                        ), 401
                    )
                )
        )
    }

    /**
     * /v4/time_registrations -stubs
     */
    private fun timeRegistrationStubs(wireMockServer: WireMockServer) {
        wireMockServer.stubFor(
            get(urlPathEqualTo("/v4/time_registrations"))
                .inScenario("scenarioTT")
                .whenScenarioStateIs(STARTED)
                .willSetStateTo("errorState")
                .willReturn(jsonResponse(objectMapper.writeValueAsString(TestData.getForecastTimeEntryResponse(
                    before = null,
                    after = null
                )), 200))
        )
        wireMockServer.stubFor(
            get(urlPathEqualTo("/v4/time_registrations"))
                .inScenario("scenarioTT")
                .whenScenarioStateIs("errorState")
                .willSetStateTo(STARTED)
                .willReturn(jsonResponse(objectMapper.writeValueAsString(ForecastErrorResponse(
                    status = 401,
                    message = "Failed to authenticate the request"
                )), 401))
        )
        wireMockServer.stubFor(
            get(urlPathEqualTo("/v4/time_registrations/updated_after/20220501T000000"))
                .inScenario("scenarioTT")
                .whenScenarioStateIs(STARTED)
                .willSetStateTo("errorStateTT")
                .willReturn(jsonResponse(objectMapper.writeValueAsString(TestData.getForecastTimeEntryResponse(
                    before = null,
                    after = "2022-05-01"
                )), 200))
        )
        wireMockServer.stubFor(
            get(urlPathEqualTo("/v4/time_registrations/updated_after/20220501T000000"))
                .inScenario("scenarioTT")
                .whenScenarioStateIs("errorState")
                .willSetStateTo(STARTED)
                .willReturn(jsonResponse(objectMapper.writeValueAsString(ForecastErrorResponse(
                    status = 401,
                    message = "Failed to authenticate the request."
                )), 401))
        )
    }

    override fun stop() {
        wireMockServer.stop()
    }
}

data class ForecastErrorResponse(
    val status: Int,
    val message: String
)