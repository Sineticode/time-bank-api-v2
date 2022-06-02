package fi.metatavu.timebank.api.tests

import fi.metatavu.timebank.api.resources.AccessTokenProvider
import fi.metatavu.timebank.api.resources.TestMockResource
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import javax.ws.rs.core.Response

/**
 * Tests for Person API
 */
@QuarkusTest
@QuarkusTestResource(TestMockResource::class)
class PersonsTest {

    val accessTokenProvider: AccessTokenProvider = AccessTokenProvider()

    /**
     * Tests listing persons
     */
    @Test
    fun listPersons() {
        given().auth().oauth2(accessTokenProvider.getAccessToken("alice"))
            .contentType("application/json")
            .`when`().get("http://localhost:8082/v1/persons")
            .then()
            .statusCode(Response.Status.OK.statusCode)
            .body("id", equalTo(TestData.getPersonA().id))
            .body("firstName", equalTo(TestData.getPersonA().firstName))
            .body("lastName", equalTo(TestData.getPersonA().lastName))
            .body("active", equalTo(TestData.getPersonA().active))
    }

    /**
     * Tests listing active persons
     */
    @Test
    fun listActivePersons() {
        given().auth().oauth2(accessTokenProvider.getAccessToken("alice"))
            .contentType("application/json")
            .`when`().get("http://localhost:8082/v1/persons?active=true")
            .then()
            .statusCode(Response.Status.OK.statusCode)
            .body("active", equalTo(TestData.getPersonA().active))
    }

    /**
     * Tests listing total time entries of given person
     */
    @Test
    fun listPersonTotalTimeEntries() {
        given().auth().oauth2(accessTokenProvider.getAccessToken("alice"))
            .contentType("application/json")
            .`when`().get("http://localhost:8082/v1/persons/${TestData.getPersonA().id}/total")
            .then()
            .statusCode(Response.Status.OK.statusCode)
            .body("id", equalTo(TestData.getPersonA().id))
    }

    /**
     * Tests listing total time entries of given person with timespan of week
     */
    @Test
    fun listPersonTotalTimeEntriesForWeek(){
        given()
            .contentType("application/json")
            .`when`().get("http://localhost:8082/v1/persons/${TestData.getPersonA().id}/total?timespan=WEEK")
            .then()
            .statusCode(Response.Status.OK.statusCode)
            .body("personId", equalTo(TestData.getTotalTimespanWeek().personId))
            .body("year", equalTo(TestData.getTotalTimespanWeek().year))
            .body("monthNumber", equalTo(TestData.getTotalTimespanWeek().monthNumber))
            .body("weekNumber", equalTo(TestData.getTotalTimespanWeek().weekNumber))
    }

    /**
     * Tests listing total time entries of given person with timespan of month
     */
    @Test
    fun listPersonTotalTimeEntriesForMonth(){
        given()
            .contentType("application/json")
            .`when`().get("http://localhost:8082/v1/persons/${TestData.getPersonA().id}/total?timespan=MONTH")
            .then()
            .statusCode(Response.Status.OK.statusCode)
            .body("personId", equalTo(TestData.getTotalTimespanMonth().personId))
            .body("year", equalTo(TestData.getTotalTimespanMonth().year))
            .body("monthNumber", equalTo(TestData.getTotalTimespanMonth().monthNumber))
            .body("weekNumber", equalTo(TestData.getTotalTimespanMonth().weekNumber))
    }

    /**
     * Tests listing total time entries of given person with timespan of year
     */
    @Test
    fun listPersonTotalTimeEntriesForYear(){
        given()
            .contentType("application/json")
            .`when`().get("http://localhost:8082/v1/persons/${TestData.getPersonA().id}/total?timespan=YEAR")
            .then()
            .statusCode(Response.Status.OK.statusCode)
            .body("personId", equalTo(TestData.getTotalTimespanYear().personId))
            .body("year", equalTo(TestData.getTotalTimespanYear().year))
            .body("monthNumber", equalTo(TestData.getTotalTimespanYear().monthNumber))
            .body("weekNumber", equalTo(TestData.getTotalTimespanYear().weekNumber))
    }
}