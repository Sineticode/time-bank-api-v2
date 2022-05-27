package fi.metatavu.timebank.api.tests

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test

/**
 * Tests for System API
 */
@QuarkusTest
class SystemTest {

    @Test
    fun testPingEndpoint() {
        given()
            .contentType("application/json")
            .`when`().get("/v1/system/ping")
            .then()
            .statusCode(200)
            .body(`is`("Pong"))
    }
}