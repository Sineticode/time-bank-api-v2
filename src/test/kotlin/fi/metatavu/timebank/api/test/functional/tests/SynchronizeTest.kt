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
            val synchronizedAfter = testBuilder.manager.synchronization.synchronizeEntries(
                before = null,
                after = "2022-05-01"
            )
            val synchronized = testBuilder.manager.synchronization.synchronizeEntries()

            setScenario(
                scenario = TIMES_SCENARIO,
                state = UPDATE_STATE
            )

            val synchronizedUpdated = testBuilder.manager.synchronization.synchronizeEntries()

            val expectedAfter = TestData.getForecastTimeEntryResponse().pageContents!!.filter { it.date > "2022-05-01" }.size
            val expectedUpdated = TestData.getUpdatedForecastTimeEntryResponse().pageContents!!.size
            val expected = TestData.getForecastTimeEntryResponse().pageContents!!.size - expectedAfter

            assertEquals(expectedAfter, synchronizedAfter.size)
            assertEquals(expectedUpdated, synchronizedUpdated.size)
            assertEquals(expected, synchronized.size)

            synchronizedAfter.forEach { synchronizedEntry ->
                testBuilder.manager.timeEntries.clean(synchronizedEntry)
            }
            synchronized.forEach { synchronizedEntry ->
                testBuilder.manager.timeEntries.clean(synchronizedEntry)
            }
            synchronizedUpdated.forEach { synchronizedEntry ->
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
            state = GENERATED_STATE
        )
        createTestBuilder().use { testBuilder ->
            val synchronized = testBuilder.manager.synchronization.synchronizeEntries()

            assertEquals(1200, synchronized.size)

            synchronized.forEach { synchronizedEntry ->
                testBuilder.manager.timeEntries.clean(synchronizedEntry)
            }
        }

    }
}