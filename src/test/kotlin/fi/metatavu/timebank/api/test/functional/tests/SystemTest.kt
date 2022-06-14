package fi.metatavu.timebank.api.test.functional.tests

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

/**
 * Tests for System API
 */
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SystemTest {

    @Test
    fun testPingEndpoint() {
        given()
            .contentType("application/json")
            .`when`().get("http://localhost:8081/v1/system/ping")
            .then()
            .statusCode(200)
            .body(`is`("Pong"))
    }
}