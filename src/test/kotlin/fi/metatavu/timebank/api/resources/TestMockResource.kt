package fi.metatavu.timebank.api.resources

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.jsonResponse
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import fi.metatavu.timebank.api.tests.TestData
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager

/**
 * Wiremock with mock data for Time Bank API
 */
class TestMockResource: QuarkusTestResourceLifecycleManager {
    private lateinit var wireMockServer: WireMockServer
    private val objectMapper = ObjectMapper()

    override fun start(): Map<String, String> {
        wireMockServer = WireMockServer(8082)
        wireMockServer.start()
        WireMock.configureFor("localhost", 8082)

        wireMockServer.stubFor(
            WireMock.get(urlEqualTo("/v1/system/ping"))
                .willReturn(
                    WireMock.aResponse()
                    .withBody("Pong")))

        personStubs(wireMockServer)
        dailyEntriesStubs(wireMockServer)

        return mapOf(Pair("forecast.base.url", wireMockServer.baseUrl()))
    }

    /**
     * Stubs for the persons related functionality
     */
    private fun personStubs (wireMockServer: WireMockServer) {
        wireMockServer.stubFor(
            WireMock.get(urlEqualTo("/v1/persons"))
                .willReturn(jsonResponse(objectMapper.writeValueAsString(TestData.getPersonA()), 200))
        )

        wireMockServer.stubFor(
            WireMock.get(urlEqualTo("/v1/persons?active=true"))
                .willReturn(jsonResponse(objectMapper.writeValueAsString(TestData.getPersonA()), 200))
        )

        wireMockServer.stubFor(
            WireMock.get(urlEqualTo("/v1/persons/${TestData.getPersonA().id}/total"))
                .willReturn(jsonResponse(objectMapper.writeValueAsString(TestData.getPersonA()), 200))
        )
    }

    /**
     * Stubs for the daily entry functionality
     */
    private fun dailyEntriesStubs (wireMockServer: WireMockServer) {
        wireMockServer.stubFor(
            WireMock.get(urlEqualTo("/v1/dailyEntries"))
                .willReturn(jsonResponse("[]", 200))
        )

        wireMockServer.stubFor(
            WireMock.get(urlEqualTo("/v1/dailyEntries?personId=${TestData.getPersonA().id}"))
                .willReturn(jsonResponse("[]", 200))
        )
    }

    override fun stop() {
        wireMockServer.stop()
    }

}