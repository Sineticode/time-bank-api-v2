package fi.metatavu.timebank.api.test.functional.tests

import fi.metatavu.timebank.api.test.functional.data.TestData
import fi.metatavu.timebank.api.test.functional.resources.TestKeycloakResource
import fi.metatavu.timebank.api.test.functional.resources.TestWiremockResource
import fi.metatavu.timebank.api.test.functional.resources.TestMySQLResource
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals

/**
 * Test for Synchronization API
 */
@QuarkusTest
@QuarkusTestResource.List(
    QuarkusTestResource(TestMySQLResource::class),
    QuarkusTestResource(TestWiremockResource::class),
    QuarkusTestResource(TestKeycloakResource::class)
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
            val synchronizeUpdated = testBuilder.manager.synchronization.synchronizeEntries()

            var expected = TestData.getForecastTimeEntryResponse().pageContents!!.size
            val expectedAfter = TestData.getForecastTimeEntryResponse().pageContents!!.filter { it.date > "2022-05-01" }.size
            expected -= expectedAfter

            assertEquals(expectedAfter, synchronizedAfter.size)
            assertEquals(expected, synchronized.size)
            assertEquals(1, synchronizeUpdated.size)

            synchronized.forEach { synchronizedEntry ->
                testBuilder.manager.timeEntries.clean(synchronizedEntry)
            }
            synchronizedAfter.forEach { synchronizedEnty ->
                testBuilder.manager.timeEntries.clean(synchronizedEnty)
            }
        }
    }

    /**
     * Tests /v1/synchronize -endpoint with 2000 random generated ForecastTimeEntries
     */
    @Test
    fun testSynchronizationLoop() {
        setScenario(
            scenario = "timesScenario",
            state = "generatedStateOne"
        )
        createTestBuilder().use { testBuilder ->
            val synchronized = testBuilder.manager.synchronization.synchronizeEntries(
                before = null,
                after = null
            )

            assertEquals(1200, synchronized.size)
        }
    }

    /**
     *  Tests /v1/synchronize -endpoint when Forecast API persons -endpoint responses with an error
     */
    @Test
    fun testSynchronizationForecastPersonsError() {
        setScenario(
            scenario = PERSONS_SCENARIO,
            state = ERROR_STATE
        )
        createTestBuilder().use { testBuilder ->

            testBuilder.manager.synchronization.assertSynchronizeFail(400)
        }
    }

    /**
     * Tests /v1/synchronize -endpoint when Forecast API time_registrations -endpoint responses with an error
     */
    @Test
    fun testSynchronizationForecastTimeRegError() {
        setScenario(
            scenario = TIMES_SCENARIO,
            state = ERROR_STATE
        )
        createTestBuilder().use { testBuilder ->

            testBuilder.manager.synchronization.assertSynchronizeFail(400)
        }
    }
}