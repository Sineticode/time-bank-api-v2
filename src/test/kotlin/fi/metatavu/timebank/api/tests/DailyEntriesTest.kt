package fi.metatavu.timebank.api.tests

import fi.metatavu.timebank.api.resources.TestMockResource
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.keycloak.client.KeycloakTestClient
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import javax.ws.rs.core.Response

/**
 * Tests for Daily Entries API
 */
@QuarkusTest
@QuarkusTestResource(TestMockResource::class)
class DailyEntriesTest {

    /**
     * Tests listing all daily entries
     */
    @Test
    fun testDailyEntriesEndPointWithoutToken() {
        given()
            .contentType("application/json")
            .`when`().get("http://localhost:8082/v1/dailyEntries")
            .then()
            .statusCode(Response.Status.UNAUTHORIZED.statusCode)
    }

    @Test
    fun testDailyEntriesEndpointWithToken() {
        given().auth().oauth2(getAccesToken("alice"))
            .`when`().get("http://localhost:8082/v1/dailyEntries")
            .then()
            .statusCode(Response.Status.OK.statusCode)
    }

    protected fun getAccesToken(userName: String): String {
        val keycloakClient = KeycloakTestClient()
        return keycloakClient.getAccessToken(userName)
    }

    /**
     * Tests listing all daily entries for specific person
     */
    @Test
    fun testDailyEntriesEndPointForPerson() {
        given()
            .contentType("application/json")
            .`when`().get("http://localhost:8082/v1/dailyEntries?personId=${TestData.getPersonA().id}")
            .then()
            .statusCode(Response.Status.OK.statusCode)
            .body("person", equalTo(TestData.getPersonA().id))
    }

  }