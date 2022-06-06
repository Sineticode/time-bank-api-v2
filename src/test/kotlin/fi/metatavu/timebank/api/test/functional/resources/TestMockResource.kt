package fi.metatavu.timebank.api.test.functional.resources

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.matching.StringValuePattern
import fi.metatavu.timebank.api.test.functional.tests.TestData
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
        objectMapper.findAndRegisterModules()
        wireMockServer = WireMockServer(8082)
        wireMockServer.start()
        configureFor("localhost", 8082)

        personStubs(wireMockServer)
        dailyEntriesStubs(wireMockServer)
//        synchronizeStubs(wireMockServer)

        return mapOf(Pair("forecast.base.url", wireMockServer.baseUrl()))
    }

    /**
     * Stubs for the persons related functionality
     */
    private fun personStubs (wireMockServer: WireMockServer) {
        wireMockServer.stubFor(
            get(urlEqualTo("/v2/persons"))
                .willReturn(jsonResponse(objectMapper.writeValueAsString(TestData.getPersons()), 200))
        )
    }

    /**
     * Stubs for the daily entry functionality
     */
    private fun dailyEntriesStubs (wireMockServer: WireMockServer) {
        wireMockServer.stubFor(
            get(urlEqualTo("/v1/holiday_calendar_entries"))
                .willReturn(jsonResponse(objectMapper.writeValueAsString(TestData.getHolidays()), 200))
        )
    }

    /**
     * Stubs for synchronization functionality
     */
    private fun synchronizeStubs (wireMockServer: WireMockServer) {
        wireMockServer.stubFor(
            post(urlEqualTo("/v1/synchronize"))
                .willReturn(jsonResponse("[]", 401))
        )

        wireMockServer.stubFor(
            post(urlEqualTo("/v1/synchronize")).withHeader(authHeader, bearerPattern)
                .willReturn(jsonResponse("[]", 200))
        )
    }

    override fun stop() {
        wireMockServer.stop()
    }

}