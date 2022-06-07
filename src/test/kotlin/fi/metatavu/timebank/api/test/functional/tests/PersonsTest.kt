package fi.metatavu.timebank.api.test.functional.tests

import fi.metatavu.timebank.api.test.functional.TestBuilder
import fi.metatavu.timebank.api.test.functional.resources.LocalTestProfile
import fi.metatavu.timebank.api.test.functional.resources.TestMockResource
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.TestProfile
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Tests for Person API
 */
@QuarkusTest
@QuarkusTestResource.List(
    QuarkusTestResource(TestMockResource::class),
    //QuarkusTestResource(TestMySQLResource::class)
)
@TestProfile(LocalTestProfile::class)
class PersonsTest {

    @Test
    fun listPersons() {
        TestBuilder().use {
            val persons = it.manager.persons.getPersons()

            assertEquals(3, persons.size)

            it.notvalid.persons.assertListFail(401)
        }
    }

    @Test
    fun listActivePersons() {
        TestBuilder().use {
            val activePersons = it.manager.persons.getActivePersons()
            assertEquals(1, activePersons.size)
            assertEquals(TestData.getPersonA().first_name, activePersons[0].firstName)

            it.notvalid.persons.assertListFail(401)
        }
    }

//    /**
//     * Tests listing persons
//     */
//    @Test
//    fun listPersonsWithoutToken() {
//        given()
//            .contentType("application/json")
//            .`when`().get("http://localhost:8082/v1/persons")
//            .then()
//            .statusCode(Response.Status.UNAUTHORIZED.statusCode)
//    }
//    @Test
//    fun listPersons() {
//        given().auth().oauth2(accessTokenProvider.getAccessToken("alice"))
//            .contentType("application/json")
//            .`when`().get("http://localhost:8082/v1/persons")
//            .then()
//            .statusCode(Response.Status.OK.statusCode)
//            .body("id", equalTo(TestData.getPersonA().id))
//            .body("firstName", equalTo(TestData.getPersonA().firstName))
//            .body("lastName", equalTo(TestData.getPersonA().lastName))
//            .body("active", equalTo(TestData.getPersonA().active))
//    }
//
//    /**
//     * Tests listing active persons
//     */
//    @Test
//    fun listActivePersonsWithoutToken() {
//        given()
//            .contentType("application/json")
//            .`when`().get("http://localhost:8082/v1/persons?active=true")
//            .then()
//            .statusCode(Response.Status.UNAUTHORIZED.statusCode)
//    }
//    @Test
//    fun listActivePersons() {
//        given().auth().oauth2(accessTokenProvider.getAccessToken("alice"))
//            .contentType("application/json")
//            .`when`().get("http://localhost:8082/v1/persons?active=true")
//            .then()
//            .statusCode(Response.Status.OK.statusCode)
//            .body("active", equalTo(TestData.getPersonA().active))
//    }
//
//    /**
//     * Tests listing total time entries of given person
//     */
//    @Test
//    fun listPersonTotalTimeEntriesWithoutToken() {
//        given()
//            .contentType("application/json")
//            .`when`().get("http://localhost:8082/v1/persons/${TestData.getPersonA().id}/total")
//            .then()
//            .statusCode(Response.Status.UNAUTHORIZED.statusCode)
//    }
//    @Test
//    fun listPersonTotalTimeEntries() {
//        given().auth().oauth2(accessTokenProvider.getAccessToken("alice"))
//            .contentType("application/json")
//            .`when`().get("http://localhost:8082/v1/persons/${TestData.getPersonA().id}/total")
//            .then()
//            .statusCode(Response.Status.OK.statusCode)
//            .body("id", equalTo(TestData.getPersonA().id))
//    }
//
//    /**
//     * Tests listing total time entries of given person with timespan of week
//     */
//    @Test
//    fun listPersonTotalTimeEntriesForWeekWithoutToken(){
//        given()
//            .contentType("application/json")
//            .`when`().get("http://localhost:8082/v1/persons/${TestData.getPersonA().id}/total?timespan=WEEK")
//            .then()
//            .statusCode(Response.Status.UNAUTHORIZED.statusCode)
//    }
//
//    @Test
//    fun listPersonTotalTimeEntriesForWeek(){
//        given().auth().oauth2(accessTokenProvider.getAccessToken("alice"))
//            .contentType("application/json")
//            .`when`().get("http://localhost:8082/v1/persons/${TestData.getPersonA().id}/total?timespan=WEEK")
//            .then()
//            .statusCode(Response.Status.OK.statusCode)
//            .body("personId", equalTo(TestData.getTotalTimespanWeek().personId))
//            .body("year", equalTo(TestData.getTotalTimespanWeek().year))
//            .body("monthNumber", equalTo(TestData.getTotalTimespanWeek().monthNumber))
//            .body("weekNumber", equalTo(TestData.getTotalTimespanWeek().weekNumber))
//    }
//
//    /**
//     * Tests listing total time entries of given person with timespan of month
//     */
//    @Test
//    fun listPersonTotalTimeEntriesForMonthWithoutToken(){
//        given()
//            .contentType("application/json")
//            .`when`().get("http://localhost:8082/v1/persons/${TestData.getPersonA().id}/total?timespan=MONTH")
//            .then()
//            .statusCode(Response.Status.UNAUTHORIZED.statusCode)
//    }
//    @Test
//    fun listPersonTotalTimeEntriesForMonth(){
//        given().auth().oauth2(accessTokenProvider.getAccessToken("alice"))
//            .contentType("application/json")
//            .`when`().get("http://localhost:8082/v1/persons/${TestData.getPersonA().id}/total?timespan=MONTH")
//            .then()
//            .statusCode(Response.Status.OK.statusCode)
//            .body("personId", equalTo(TestData.getTotalTimespanMonth().personId))
//            .body("year", equalTo(TestData.getTotalTimespanMonth().year))
//            .body("monthNumber", equalTo(TestData.getTotalTimespanMonth().monthNumber))
//            .body("weekNumber", equalTo(TestData.getTotalTimespanMonth().weekNumber))
//    }
//
//    /**
//     * Tests listing total time entries of given person with timespan of year
//     */
//    @Test
//    fun listPersonTotalTimeEntriesForYearWithoutToken(){
//        given()
//            .contentType("application/json")
//            .`when`().get("http://localhost:8082/v1/persons/${TestData.getPersonA().id}/total?timespan=YEAR")
//            .then()
//            .statusCode(Response.Status.UNAUTHORIZED.statusCode)
//    }
//    @Test
//    fun listPersonTotalTimeEntriesForYear(){
//        given().auth().oauth2(accessTokenProvider.getAccessToken("alice"))
//            .contentType("application/json")
//            .`when`().get("http://localhost:8082/v1/persons/${TestData.getPersonA().id}/total?timespan=YEAR")
//            .then()
//            .statusCode(Response.Status.OK.statusCode)
//            .body("personId", equalTo(TestData.getTotalTimespanYear().personId))
//            .body("year", equalTo(TestData.getTotalTimespanYear().year))
//            .body("monthNumber", equalTo(TestData.getTotalTimespanYear().monthNumber))
//            .body("weekNumber", equalTo(TestData.getTotalTimespanYear().weekNumber))
//    }
}