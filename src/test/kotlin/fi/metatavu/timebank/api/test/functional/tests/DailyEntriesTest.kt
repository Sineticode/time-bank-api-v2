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
  }