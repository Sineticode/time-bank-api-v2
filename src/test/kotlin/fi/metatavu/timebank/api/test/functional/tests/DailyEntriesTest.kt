package fi.metatavu.timebank.api.test.functional.tests

import fi.metatavu.timebank.api.test.functional.TestBuilder
import fi.metatavu.timebank.api.test.functional.resources.LocalTestProfile
import fi.metatavu.timebank.api.test.functional.resources.TestMockResource
import fi.metatavu.timebank.api.test.functional.resources.TestMySQLResource
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.TestProfile
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

/**
 * Tests for Daily Entries API
 */
@QuarkusTest
@QuarkusTestResource.List(
    QuarkusTestResource(TestMySQLResource::class),
    QuarkusTestResource(TestMockResource::class)
)
@TestProfile(LocalTestProfile::class)
@TestClassOrder(ClassOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Order(2)
class DailyEntriesTest {

    /**
     * Runs synchronization to make sure that test database is populated before all tests.
     */
    @BeforeAll
    fun runSynchronizationBeforeTests() {
        TestBuilder().use { testBuilder ->
            testBuilder.manager.synchronization.synchronizeEntries()
        }
    }

    /**
     * Tests /v1/dailyEntries -endpoint
     */
    @Test
    fun testDailyEntries() {
        TestBuilder().use{ testBuilder ->

            val expectedAmount = TestData.getForecastTimeEntryResponse().pageContents!!.groupBy { forecastTimeEntry ->
                Pair(forecastTimeEntry.date, forecastTimeEntry.person)
            }.values.size

            val dailyEntries = testBuilder.manager.dailyEntries.getDailyEntries(
                personId = null,
                before = null,
                after = null
            )

            assertEquals(expectedAmount, dailyEntries.size)

        }
    }

    /**
     * Tests listing dailyEntry for non-existing user and existing user with invalid params
     */
    @Test
    fun listDailyEntriesFails() {
        TestBuilder().use { testBuilder ->
            testBuilder.manager.dailyEntries.assertListFail(
                expectedStatus = 404,
                id = 12345658,
                before = null,
                after = null
            )
            testBuilder.manager.dailyEntries.assertListFail(
                expectedStatus = 404,
                id = 1,
                before = "NOT_VALID_DATE",
                after = null
            )
            testBuilder.manager.dailyEntries.assertListFail(
                expectedStatus = 404,
                id = 1,
                before = null,
                after = "NOT_VALID_DATE"
            )
            testBuilder.manager.dailyEntries.assertListFail(
                expectedStatus = 404,
                id = 1,
                before = "NOT_VALID_DATE",
                after = "NOT_VALID_DATE"
            )
        }
    }

    /**
     * Tests /v1/dailyEntries -endpoint without access token
     */
    @Test
    fun listDailyEntriesWithNullToken(){
        TestBuilder().use { testBuilder ->
            testBuilder.userWithNullToken.dailyEntries.assertListFailWithNullToken(
                expectedStatus = 401
            )
        }
    }
}