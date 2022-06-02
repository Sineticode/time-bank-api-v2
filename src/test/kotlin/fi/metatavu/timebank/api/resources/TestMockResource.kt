package fi.metatavu.timebank.api.resources

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.matching.StringValuePattern
import fi.metatavu.timebank.api.tests.TestData
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager

/**
 * Wiremock with mock data for Time Bank API
 */
class TestMockResource: QuarkusTestResourceLifecycleManager {
    private lateinit var wireMockServer: WireMockServer
    private val objectMapper = ObjectMapper()

    private val authHeader = "Authorization"
    private val bearerPattern: StringValuePattern = containing("Bearer")

    override fun start(): Map<String, String> {
        wireMockServer = WireMockServer(8082)
        wireMockServer.start()
        configureFor("localhost", 8082)

        wireMockServer.stubFor(
            get(urlEqualTo("/v1/system/ping")).withHeader(authHeader, bearerPattern)
                .willReturn(aResponse()
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
            get(urlEqualTo("/v1/persons")).withHeader(authHeader, bearerPattern)
                .willReturn(jsonResponse(objectMapper.writeValueAsString(TestData.getPersonA()), 200))
        )

        wireMockServer.stubFor(
           get(urlEqualTo("/v1/persons?active=true")).withHeader(authHeader, bearerPattern)
                .willReturn(jsonResponse(objectMapper.writeValueAsString(TestData.getPersonA()), 200))
        )

        wireMockServer.stubFor(
            get(urlEqualTo("/v1/persons/${TestData.getPersonA().id}/total")).withHeader(authHeader, bearerPattern)
                .willReturn(jsonResponse(objectMapper.writeValueAsString(TestData.getPersonA()), 200))
        )

        wireMockServer.stubFor(
            WireMock.get(urlEqualTo("/v1/persons/${TestData.getPersonA().id}/total?timespan=WEEK"))
                .willReturn(jsonResponse(objectMapper.writeValueAsString(TestData.getTotalTimespanWeek()), 200))
        )

        wireMockServer.stubFor(
            WireMock.get(urlEqualTo("/v1/persons/${TestData.getPersonA().id}/total?timespan=MONTH"))
                .willReturn(jsonResponse(objectMapper.writeValueAsString(TestData.getTotalTimespanMonth()), 200))
        )

        wireMockServer.stubFor(
            WireMock.get(urlEqualTo("/v1/persons/${TestData.getPersonA().id}/total?timespan=YEAR"))
                .willReturn(jsonResponse(objectMapper.writeValueAsString(TestData.getTotalTimespanYear()), 200))
        )
    }

    /**
     * Stubs for the daily entry functionality
     */
    private fun dailyEntriesStubs (wireMockServer: WireMockServer) {
        wireMockServer.stubFor(
            get(urlEqualTo("/v1/dailyEntries"))
                .willReturn(jsonResponse("[]", 401))
        )

        wireMockServer.stubFor(
            get(urlEqualTo("/v1/dailyEntries")).withHeader(authHeader, bearerPattern)
                .willReturn(jsonResponse("[]", 200))
        )

        wireMockServer.stubFor(
            get(urlEqualTo("/v1/dailyEntries?personId=${TestData.getPersonA().id}")).withHeader(authHeader, bearerPattern)
                .willReturn(jsonResponse(objectMapper.writeValueAsString(TestData.getDailyEntryA()), 200))
        )
    }

    override fun stop() {
        wireMockServer.stop()
    }

}