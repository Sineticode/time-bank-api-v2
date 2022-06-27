package fi.metatavu.timebank.api.test.functional.tests

import fi.metatavu.timebank.api.test.functional.resources.TestKeycloakResource
import fi.metatavu.timebank.api.test.functional.resources.TestMySQLResource
import fi.metatavu.timebank.api.test.functional.resources.TestWiremockResource
import fi.metatavu.timebank.test.client.models.TimeEntry
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
    QuarkusTestResource(TestWiremockResource::class),
    QuarkusTestResource(TestKeycloakResource::class)
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DailyEntriesTest: AbstractTest() {

    var synchronized = listOf<TimeEntry>()

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
            synchronized = testBuilder.manager.synchronization.synchronizeEntries().toList()
        }
    }

    /**
     * Cleans persisted timeEntries after all tests
     */
    @AfterAll
    fun cleanSynchronized() {
        createTestBuilder().use { testBuilder ->
            synchronized.forEach { synchronizedEntry ->
                testBuilder.manager.timeEntries.clean(synchronizedEntry)
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
            assertEquals(7, entriesBefore.size)
            assertEquals(7, entriesAfter.size)
        }
    }
}