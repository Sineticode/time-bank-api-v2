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
}