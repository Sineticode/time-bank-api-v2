package fi.metatavu.timebank.api.resources

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import fi.metatavu.timebank.api.tests.TestData
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import org.junit.jupiter.api.BeforeEach

/**
 * Wiremock with mock data for Time Bank API
 */
class PersonsMock: QuarkusTestResourceLifecycleManager {
    private lateinit var wireMockServer: WireMockServer
    private val objectMapper = ObjectMapper()

    override fun start(): Map<String, String> {
        wireMockServer = WireMockServer(8082)
        wireMockServer.start()
        configureFor("localhost",8082)

        wireMockServer.stubFor(
            get(urlEqualTo("/v1/system/ping"))
                .willReturn(aResponse()
                    .withBody("Pong")))

        personStubs(wireMockServer)


        return mapOf(Pair("forecast.base.url", wireMockServer.baseUrl()))
    }

    private fun personStubs (wireMockServer: WireMockServer) {
        wireMockServer.stubFor(
            get(urlEqualTo("/v1/persons"))
                .willReturn(jsonResponse(objectMapper.writeValueAsString(TestData.getPersonA()), 200))
        )

        wireMockServer.stubFor(
            get(urlEqualTo("/v1/persons?active=true"))
                .willReturn(jsonResponse("[]", 200))
        )

        wireMockServer.stubFor(
            get(urlEqualTo("/v1/persons/${TestData.personId}/total"))
                .willReturn(jsonResponse("[]", 200))
        )
    }

    @BeforeEach
    fun clearMocks() {
        wireMockServer.resetAll()
    }

    override fun stop() {
        wireMockServer.stop()
    }
}