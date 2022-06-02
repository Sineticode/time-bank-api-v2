package fi.metatavu.timebank.api.tests

import fi.metatavu.timebank.api.resources.AccessTokenProvider
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test

/**
 * Tests for System API
 */
@QuarkusTest
class SystemTest {

    val accessTokenProvider: AccessTokenProvider = AccessTokenProvider()

    @Test
    fun testPingEndpoint() {
        given()
            .contentType("application/json").auth().oauth2(accessTokenProvider.getAccessToken("alice"))
            .`when`().get("http://localhost:8082/v1/system/ping")
            .then()
            .statusCode(200)
            .body(`is`("Pong"))
    }
}