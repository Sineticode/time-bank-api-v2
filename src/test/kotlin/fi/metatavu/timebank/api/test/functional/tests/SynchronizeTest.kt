package fi.metatavu.timebank.api.test.functional.tests

import fi.metatavu.timebank.api.test.functional.data.TestData
import fi.metatavu.timebank.api.test.functional.resources.TestMySQLResource
import fi.metatavu.timebank.api.test.functional.resources.TestWiremockResource
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.Assertions.assertEquals

/**
 * Tests for Synchronization API
 */
@QuarkusTest
@QuarkusTestResource.List(
    QuarkusTestResource(TestMySQLResource::class),
    QuarkusTestResource(TestWiremockResource::class)
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SynchronizeTest: AbstractTest() {

    /**
     * Resets Wiremock scenario states before each test
     */
    @BeforeEach
    fun resetScenariosBeforeEach() {
        resetScenarios()
    }

    /**
     * Tests /v1/synchronize -endpoint
     */
    @Test
    fun testSynchronization() {
        createTestBuilder().use { testBuilder ->
            testBuilder.manager.synchronization.synchronizeEntries(
                before = null,
                after = "2022-05-01"
            )

            val synchronizedAfter = testBuilder.manager.timeEntries.getTimeEntries()

            synchronizedAfter.forEach { synchronizedEntry ->
                testBuilder.manager.timeEntries.clean(synchronizedEntry)
            }

            testBuilder.manager.synchronization.synchronizeEntries()

            setScenario(
                scenario = TIMES_SCENARIO,
                state = UPDATE_STATE_ONE
            )

            testBuilder.manager.synchronization.synchronizeEntries()

            val synchronizedAll = testBuilder.manager.timeEntries.getTimeEntries()

            val expectedAfter = TestData.getForecastTimeEntryResponse().pageContents!!.filter { it.date > "2022-05-01" }.size
            val expected = TestData.getForecastTimeEntryResponse().pageContents!!.size

            assertEquals(expectedAfter, synchronizedAfter.size)
            assertEquals(expected, synchronizedAll.size)
            synchronizedAll.forEach { synchronizedEntry ->
                testBuilder.manager.timeEntries.clean(synchronizedEntry)
            }
        }
    }

    /**
     * Tests /v1/synchronize -endpoint with 2000 random generated ForecastTimeEntries
     */
    @Test
    fun testSynchronizationLoop() {
        setScenario(
            scenario = TIMES_SCENARIO,
            state = GENERATED_STATE_ONE
        )
        createTestBuilder().use { testBuilder ->
            testBuilder.manager.synchronization.synchronizeEntries()
            val timeEntries = testBuilder.manager.timeEntries.getTimeEntries()
            assertEquals(1200, timeEntries.size)

            timeEntries.forEach { timeEntry ->
                testBuilder.manager.timeEntries.clean(timeEntry)
            }
        }

    }
}