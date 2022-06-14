package fi.metatavu.timebank.api.test.functional.tests

import fi.metatavu.timebank.api.test.functional.TestBuilder
import fi.metatavu.timebank.api.test.functional.resources.LocalTestProfile
import fi.metatavu.timebank.api.test.functional.resources.TestMockResource
import fi.metatavu.timebank.api.test.functional.resources.TestMySQLResource
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.TestProfile
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals

/**
 * Test for Synchronization API
 */
@QuarkusTest
@QuarkusTestResource.List(
    QuarkusTestResource(TestMySQLResource::class),
    QuarkusTestResource(TestMockResource::class)
)
@TestProfile(LocalTestProfile::class)
@TestClassOrder(ClassOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Order(1)
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
        TestBuilder().use { testBuilder ->
            val synchronizedAfter = testBuilder.manager.synchronization.synchronizeEntries(
                before = null,
                after = "2022-05-01"
            )
            val synchronized = testBuilder.manager.synchronization.synchronizeEntries()
            val synchronizedNotFound = testBuilder.manager.synchronization.synchronizeEntries()
            setScenario(
                scenario = TIMES_SCENARIO,
                state = UPDATE_STATE
            )
            val synchronizeUpdated = testBuilder.manager.synchronization.synchronizeEntries()

            var expected = TestData.getForecastTimeEntry().size
            val expectedAfter = TestData.getForecastTimeEntry().filter { it.date > "2022-05-01" }.size
            expected -= expectedAfter

            assertEquals(expectedAfter, synchronizedAfter.message)
            assertEquals(expected, synchronized.message)
            assertEquals(0, synchronizedNotFound.message)
            assertEquals(1, synchronizeUpdated.message)
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
        TestBuilder().use { testBuilder ->
            val synchronized = testBuilder.manager.synchronization.synchronizeEntries(
                before = null,
                after = null
            )

            assertEquals(2000, synchronized.message)
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
        TestBuilder().use { testBuilder ->

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
        TestBuilder().use { testBuilder ->

            testBuilder.manager.synchronization.assertSynchronizeFail(400)
        }
    }
}