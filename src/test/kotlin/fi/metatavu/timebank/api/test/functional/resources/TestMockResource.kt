package fi.metatavu.timebank.api.test.functional.resources

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.matching.StringValuePattern
import com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED
import fi.metatavu.timebank.api.test.functional.tests.TestData
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import org.testcontainers.shaded.org.zeroturnaround.exec.StartedProcess
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.MediaType.TEXT_PLAIN

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
        holidayStubs(wireMockServer)
        timeRegistrationsStubs(wireMockServer)

        return mapOf(Pair("forecast.base.url", wireMockServer.baseUrl()))
    }

    /**
     * Stubs for Forecast persons endpoint used in different functionalities
     */
    private fun personStubs(wireMockServer: WireMockServer) {
        wireMockServer.stubFor(
            get(urlEqualTo("/v2/persons"))
                .willReturn(jsonResponse(objectMapper.writeValueAsString(TestData.getPersons()), 200))
        )
    }

    /**
     * Stubs for Forecast holiday_calendar_entries endpoint used in daily entry functionality
     */
    private fun holidayStubs(wireMockServer: WireMockServer) {
        wireMockServer.stubFor(
            get(urlEqualTo("/v1/holiday_calendar_entries"))
                .willReturn(jsonResponse(objectMapper.writeValueAsString(TestData.getHolidays()), 200))
        )
    }

    /**
     * Stubs for Forecast time_registrations endpoint used in synchronization functionality
     */
    private fun timeRegistrationsStubs(wireMockServer: WireMockServer) {
        wireMockServer.stubFor(
            get(urlPathEqualTo("/v4/time_registrations"))
                .willReturn(jsonResponse(objectMapper.writeValueAsString(TestData.getForecastTimeEntryResponse()), 200))
        )
    }

    override fun stop() {
        wireMockServer.stop()
    }

}