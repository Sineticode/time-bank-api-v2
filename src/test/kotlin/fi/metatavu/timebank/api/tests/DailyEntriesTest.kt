package fi.metatavu.timebank.api.tests

import fi.metatavu.timebank.api.resources.TestMockResource
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
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
    fun testDailyEntriesEndPoint() {
        given()
            .contentType("application/json")
            .`when`().get("/v1/dailyEntries")
            .then()
            .statusCode(200)
    }

    /**
     * Tests listing all daily entries for specific person
     */
    @Test
    fun testDailyEntriesEndPointForPerson() {
        given()
            .contentType("application/json")
            .`when`().get("/v1/dailyEntries?personId=${TestData.getPersonA().id}")
            .then()
            .statusCode(Response.Status.OK.statusCode)
    }

  }