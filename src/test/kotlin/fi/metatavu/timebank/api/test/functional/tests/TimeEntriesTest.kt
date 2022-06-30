package fi.metatavu.timebank.api.test.functional.tests

import fi.metatavu.timebank.api.test.functional.resources.TestMySQLResource
import fi.metatavu.timebank.api.test.functional.resources.TestWiremockResource
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

/**
 * Tests for TimeEntries API
 */
@QuarkusTest
@QuarkusTestResource.List(
    QuarkusTestResource(TestMySQLResource::class),
    QuarkusTestResource(TestWiremockResource::class)
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TimeEntriesTest: AbstractTest() {

    /**
     * Resets Wiremock scenario states before each test
     */
    @BeforeEach
    fun resetScenariosBeforeEach() {
        resetScenarios()
    }

    /**
     * Tests /v1/timeEntries -endpoint DELETE method
     */
    @Test
    fun testTimeEntriesDelete() {
        createTestBuilder().use { testBuilder ->
            val synchronized = testBuilder.manager.synchronization.synchronizeEntries(
                before = "2022-04-01"
            )

            synchronized.forEach { timeEntry ->
                testBuilder.userA.timeEntries.assertDeleteFail(401, timeEntry.entryId)
            }
            synchronized.forEach { timeEntry ->
                testBuilder.manager.timeEntries.clean(timeEntry)
            }
        }
    }
}