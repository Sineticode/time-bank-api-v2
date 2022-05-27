package fi.metatavu.timebank.api.resources

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

    override fun start(): Map<String, String> {
        wireMockServer = WireMockServer(8082)
        wireMockServer.start()
        configureFor("localhost",8082)

        //RestAssured.defaultParser = Parser.JSON;

        wireMockServer.stubFor(
            get(urlEqualTo("/v1/system/ping"))
                .willReturn(aResponse()
                    .withBody("Pong")))

        personStubs(wireMockServer)


        return mapOf(Pair("forecast.api.baseUrl", wireMockServer.baseUrl()))
    }

    private fun personStubs (wireMockServer: WireMockServer) {
        wireMockServer.stubFor(
            get(urlEqualTo("/v1/persons"))
                .willReturn(aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody(TestData.personData)
                )
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