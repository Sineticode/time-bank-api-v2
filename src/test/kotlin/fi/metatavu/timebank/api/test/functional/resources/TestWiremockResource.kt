package fi.metatavu.timebank.api.test.functional.resources

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED
import fi.metatavu.timebank.api.test.functional.data.TestData
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager

/**
 * Wiremock to mock Forecast API
 */
class TestWiremockResource: QuarkusTestResourceLifecycleManager {
    private var wireMockServer = WireMockServer()
    private val objectMapper = ObjectMapper()

    data class ForecastErrorResponse(val status: Int, val message: String)

    override fun start(): Map<String, String> {
        objectMapper.findAndRegisterModules()
        wireMockServer = WireMockServer(8082)
        wireMockServer.start()
        configureFor("localhost", 8082)
        println("WireMockServer.baseUrl: ${ wireMockServer.baseUrl() }")

        personsStubs(wireMockServer)

        return mapOf(Pair("forecast.base.url", wireMockServer.baseUrl()))
    }

    /**
     * /v2/persons -stubs
     */
    private fun personsStubs(wireMockServer: WireMockServer) {
        wireMockServer.stubFor(
            get(urlPathEqualTo("/v2/persons"))
                .inScenario("personsScenario")
                .whenScenarioStateIs(STARTED)
                .willReturn(jsonResponse(objectMapper.writeValueAsString(TestData.getPersons()), 200))
        )

        wireMockServer.stubFor(
            get(urlPathEqualTo("/v2/persons"))
                .inScenario("personsScenario")
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

    override fun stop() {
        wireMockServer.stop()
    }
}