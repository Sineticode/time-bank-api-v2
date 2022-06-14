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
class DailyEntriesTest: AbstractTest() {

    /**
     * Runs synchronization to make sure that test database is populated before all tests.
     */
    @BeforeAll
    fun runSynchronizationBeforeTests() {
        resetScenarios()
        createTestBuilder().use { testBuilder ->
            testBuilder.manager.synchronization.synchronizeEntries()
        }
    }

    /**
     * Resets Wiremock scenario states before each test
     */
    @BeforeEach
    fun resetScenariosBeforeEach() {
        resetScenarios()
    }

    /**
     * Tests /v1/dailyEntries -endpoint
     */
     @Test
    fun testDailyEntries() {
        createTestBuilder().use { testBuilder ->
            val expectedAmount = TestData.getForecastTimeEntryResponse().pageContents!!.groupBy { Pair(it.date, it.person) }.values.size

            val dailyEntries = testBuilder.manager.dailyEntries.getDailyEntries(
                personId = null,
                before = null,
                after = null
            )

            assertEquals(expectedAmount, dailyEntries.size)

        }
    }

    /**
     * Tests /v1/dailyEntries?personId -endpoint
     */
     @Test
    fun testDailyEntriesForPersonA() {
        createTestBuilder().use { testBuilder ->
            val expectedAmount = TestData.getForecastTimeEntryResponse().pageContents!!.filter { forecastTimeEntry ->
                forecastTimeEntry.person == TestData.getPerson(id = 1).id
            }.size
            val dailyEntries = testBuilder.manager.dailyEntries.getDailyEntries(
                personId = 1,
                before = null,
                after = null
            )

            assertEquals(expectedAmount, dailyEntries.size)
        }
    }

    /**
     * Tests /v1/dailyEntries?before -endpoint
     */
     @Test
    fun testDailyEntriesWithBefore() {
        createTestBuilder().use { testBuilder ->
            val expectedAmount = TestData.getForecastTimeEntryResponse(
                before = "2022-05-12"
            ).pageContents!!.size
            val dailyEntries = testBuilder.manager.dailyEntries.getDailyEntries(
                personId = null,
                before = "2022-05-12",
                after = null
            )

            assertEquals(expectedAmount, dailyEntries.size)
        }
    }

    /**
     * Tests /v1/dailyEntries?after -endpoint
     */
     @Test
    fun testDailyEntriesWithAfter() {
        createTestBuilder().use { testBuilder ->
            val expectedAmount = TestData.getForecastTimeEntryResponse(
                after = "2022-05-12"
            ).pageContents!!.size
            val dailyEntries = testBuilder.manager.dailyEntries.getDailyEntries(
                personId = null,
                before = null,
                after = "2022-05-12"
            )

            assertEquals(expectedAmount, dailyEntries.size)
        }
    }

    /**
     * Tests listing dailyEntry for non-existing user and existing user with invalid params
     */
     @Test
    fun listDailyEntriesFails() {
        createTestBuilder().use { testBuilder ->
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
     * Tests /v1/dailyEntries -endpoint when Forecast API persons -endpoint responses with an error
     */
     @Test
    fun testDailyEntriesForecastPersonsError() {
        setScenario(
            scenario = PERSONS_SCENARIO,
            state = ERROR_STATE
        )
        createTestBuilder().use { testBuilder ->
            testBuilder.manager.dailyEntries.assertListFail(
                expectedStatus = 400,
                id = null,
                before = null,
                after = null
            )
        }
    }

    /**
     * Tests /v1/dailyEntries -endpoint when Forecast API holidays -endpoint responses with an error
     */
     @Test
    fun testDailyEntriesForecastHolidaysError() {
        setScenario(
            scenario = HOLIDAYS_SCENARIO,
            state = ERROR_STATE
        )
        createTestBuilder().use { testBuilder ->
            testBuilder.manager.dailyEntries.assertListFail(
                expectedStatus = 400,
                id = null,
                before = null,
                after = null
            )
        }
    }

    /**
     * Tests /v1/dailyEntries -endpoint without access token
     */
     @Test
    fun listDailyEntriesWithNullToken(){
        createTestBuilder().use { testBuilder ->
            testBuilder.userWithNullToken.dailyEntries.assertListFail(
                expectedStatus = 401,
                id = null,
                before = null,
                after = null
            )
        }
    }
}