package fi.metatavu.timebank.api.test.functional.tests

import fi.metatavu.timebank.api.test.functional.TestBuilder
import fi.metatavu.timebank.api.test.functional.resources.LocalTestProfile
import fi.metatavu.timebank.api.test.functional.resources.TestMockResource
import fi.metatavu.timebank.api.test.functional.resources.TestMySQLResource
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.TestProfile
import org.junit.jupiter.api.Test

/**
 * Tests for Daily Entries API
 */
@QuarkusTest
@QuarkusTestResource.List(
    QuarkusTestResource(TestMockResource::class),
    //QuarkusTestResource(TestMySQLResource::class)
)
@TestProfile(LocalTestProfile::class)
class DailyEntriesTest {

    @Test
    fun testDailyEntries() {
        TestBuilder().use {
            val dailyEntries = it.manager.dailyEntries.getDailyEntries()
            dailyEntries.forEach { i ->
                println(i)
            }
        }
    }
//    /**
//     * Tests listing all daily entries
//     */
//    @Test
//    fun testDailyEntriesEndPointWithoutToken() {
//        given()
//            .contentType("application/json")
//            .`when`().get("http://localhost:8082/v1/dailyEntries")
//            .then()
//            .statusCode(Response.Status.UNAUTHORIZED.statusCode)
//    }
//
//    @Test
//    fun testDailyEntriesEndpointWithToken() {
//        given().auth().oauth2(accessTokenProvider.getAccessToken("alice"))
//            .`when`().get("http://localhost:8082/v1/dailyEntries")
//            .then()
//            .statusCode(Response.Status.OK.statusCode)
//    }
//
//    /**
//     * Tests listing all daily entries for specific person
//     */
//    @Test
//    fun testDailyEntriesEndPointForPerson() {
//        given().auth().oauth2(accessTokenProvider.getAccessToken("alice"))
//            .contentType("application/json")
//            .`when`().get("http://localhost:8082/v1/dailyEntries?personId=${TestData.getPersonA().id}")
//            .then()
//            .statusCode(Response.Status.OK.statusCode)
//            .body("person", equalTo(TestData.getPersonA().id))
//    }

  }