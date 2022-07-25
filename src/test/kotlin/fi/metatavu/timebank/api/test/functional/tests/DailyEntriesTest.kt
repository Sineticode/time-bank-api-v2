package fi.metatavu.timebank.api.test.functional.tests

import fi.metatavu.timebank.api.test.functional.resources.TestMySQLResource
import fi.metatavu.timebank.api.test.functional.resources.TestWiremockResource
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

/**
 * Tests for DailyEntries API
 */
@QuarkusTest
@QuarkusTestResource.List(
    QuarkusTestResource(TestMySQLResource::class),
    QuarkusTestResource(TestWiremockResource::class)
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DailyEntriesTest: AbstractTest() {

    /**
     * Resets Wiremock scenario states before each test
     */
    @BeforeEach
    fun resetScenariosBeforeEach() {
        resetScenarios()
    }

    /**
     * Runs synchronization before tests to make sure database is populated
     */
    @BeforeAll
    fun synchronizeBeforeTests() {
        createTestBuilder().use { testBuilder ->
            testBuilder.manager.synchronization.synchronizeEntries()
        }
    }

    /**
     * Cleans persisted timeEntries after all tests
     */
    @AfterAll
    fun cleanSynchronized() {
        createTestBuilder().use { testBuilder ->
            val entries = testBuilder.manager.timeEntries.getTimeEntries()
            entries.forEach { timeEntry ->
                testBuilder.manager.timeEntries.clean(timeEntry)
            }
        }
    }

    /**
     * Tests /v1/dailyEntries -endpoint
     */
    @Test
    fun listDailyEntries() {
        createTestBuilder().use { testBuilder ->
            val allEntries = testBuilder.manager.dailyEntries.getDailyEntries()
            val entriesForPersonA = testBuilder.manager.dailyEntries.getDailyEntries(personId = 1)
            val entriesBefore = testBuilder.manager.dailyEntries.getDailyEntries(before = "2022-04-30")
            val entriesAfter = testBuilder.manager.dailyEntries.getDailyEntries(after = "2022-05-01")

            assertEquals(15, allEntries.size)
            assertEquals(4, entriesForPersonA.size)
            assertEquals(8, entriesBefore.size)
            assertEquals(7, entriesAfter.size)
        }
    }

    /**
     * Tests /v1/dailyEntries?personId=5 -endpoint
     */
    @Test
    fun testWorktimeCalendars() {
        createTestBuilder().use { testBuilder ->
            val firstEntries = testBuilder.manager.dailyEntries.getDailyEntries(personId = 5)

            setScenario(
                scenario = "personsScenario",
                state = "updateStateOne"
            )
            setScenario(
                scenario = "timesScenario",
                state = "updateStateTwo"
            )

            testBuilder.manager.synchronization.synchronizeEntries()
            val secondEntries = testBuilder.manager.dailyEntries.getDailyEntries(personId = 5)

            assertEquals(1, firstEntries.size)
            assertEquals(2, secondEntries.size)
            assertEquals(435, firstEntries[0].expected)
            assertEquals(435, secondEntries[1].expected)
            assertEquals(217, secondEntries[0].expected)
        }
    }
}