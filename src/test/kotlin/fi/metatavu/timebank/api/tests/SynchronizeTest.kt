package fi.metatavu.timebank.api.tests

import fi.metatavu.timebank.api.resources.AccessTokenProvider
import fi.metatavu.timebank.api.resources.TestMockResource
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.junit.jupiter.api.Test
import javax.ws.rs.core.Response

@QuarkusTest
@QuarkusTestResource(TestMockResource::class)

class SynchronizeTest {

    val accessTokenProvider: AccessTokenProvider = AccessTokenProvider()

    /**
     *Test sending synchronize post request
     */

    @Test
    fun synchronizeWithoutToken() {
        given()
            .contentType("application/json")
            .`when`().post("http://localhost:8082/v1/synchronize")
            .then()
            .statusCode(Response.Status.UNAUTHORIZED.statusCode)
    }

    @Test
    fun synchronizeWithToken() {
        given().auth().oauth2(accessTokenProvider.getAccessToken("alice"))
            .contentType("application/json")
            .`when`().post("http://localhost:8082/v1/synchronize")
            .then()
            .statusCode(Response.Status.OK.statusCode)
    }

}