package fi.metatavu.timebank.api.test.functional.tests

import fi.metatavu.timebank.api.test.functional.resources.AccessTokenProvider
import fi.metatavu.timebank.api.test.functional.resources.TestMockResource
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
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

    val accessTokenProvider: AccessTokenProvider = AccessTokenProvider()

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
        given().auth().oauth2(accessTokenProvider.getAccessToken("alice"))
            .`when`().get("http://localhost:8082/v1/dailyEntries")
            .then()
            .statusCode(Response.Status.OK.statusCode)
    }

    /**
     * Tests listing all daily entries for specific person
     */
    @Test
    fun testDailyEntriesEndPointForPerson() {
        given().auth().oauth2(accessTokenProvider.getAccessToken("alice"))
            .contentType("application/json")
            .`when`().get("http://localhost:8082/v1/dailyEntries?personId=${TestData.getPersonA().id}")
            .then()
            .statusCode(Response.Status.OK.statusCode)
            .body("person", equalTo(TestData.getPersonA().id))
    }

  }