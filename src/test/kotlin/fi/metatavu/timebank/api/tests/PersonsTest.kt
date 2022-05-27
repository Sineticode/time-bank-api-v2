package fi.metatavu.timebank.api.tests

import fi.metatavu.timebank.api.resources.PersonsMock
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
@QuarkusTestResource(PersonsMock::class)
class PersonTest {

    /**
     * Tests listing persons
     */
    @Test
    fun listPersons() {
        given()
            .contentType("application/json")
            .`when`().get("/v1/persons")
            .then()
            .statusCode(Response.Status.OK.statusCode)
    }

    /**
     * Tests listing active persons
     */
    @Test
    fun listActivePersons() {
        given()
            .contentType("application/json")
            .`when`().get("/v1/persons?active=true")
            .then()
            .statusCode(Response.Status.OK.statusCode)
    }

    /**
     * Tests listing total time entries of given person
     */
    @Test
    fun listPersonTotalTimeEntries() {
        given()
            .contentType("application/json")
            .`when`().get("/v1/persons/${TestData.personId}/total")
            .then()
            .statusCode(Response.Status.OK.statusCode)
            .body("personId", equalTo(TestData.personId))
    }

    @Test
    fun personTestWithWiremock() {
        given()
            .contentType("application/json")
            .`when`().get("/v1/persons")
            .then()
            .statusCode(Response.Status.OK.statusCode)
            .body("personId", equalTo(1234))
            .body("firstName", equalTo("Test"))
            .body("lastName", equalTo("Tester"))
            .body("email", equalTo("test.tester@metatavu.fi"))
            .body("active", equalTo("true"))
    }
}