package fi.metatavu.timebank.api.test.functional.tests

import fi.metatavu.timebank.api.test.functional.resources.AccessTokenProvider
import fi.metatavu.timebank.api.test.functional.resources.LocalTestProfile
import fi.metatavu.timebank.api.test.functional.resources.TestMockResource
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.TestProfile

/**
 * Test for Synchronization API
 */
@QuarkusTest
@QuarkusTestResource.List(
    QuarkusTestResource(TestMockResource::class),
    //QuarkusTestResource(TestMySQLResource::class)
)
@TestProfile(LocalTestProfile::class)
class SynchronizeTest {

    val accessTokenProvider: AccessTokenProvider = AccessTokenProvider()

//    /**
//     *Test sending synchronize post request
//     */
//
//    @Test
//    fun synchronizeWithoutToken() {
//        given()
//            .contentType("application/json")
//            .`when`().post("http://localhost:8082/v1/synchronize")
//            .then()
//            .statusCode(Response.Status.UNAUTHORIZED.statusCode)
//    }
//
//    @Test
//    fun synchronizeWithToken() {
//        given().auth().oauth2(accessTokenProvider.getAccessToken("alice"))
//            .contentType("application/json")
//            .`when`().post("http://localhost:8082/v1/synchronize")
//            .then()
//            .statusCode(Response.Status.OK.statusCode)
//    }

}