package fi.metatavu.timebank.api.test.functional.tests

import fi.metatavu.timebank.api.test.functional.resources.LocalTestProfile
import fi.metatavu.timebank.api.test.functional.resources.TestMySQLResource
import fi.metatavu.timebank.api.test.functional.resources.TestWiremockResource
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.TestProfile
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import fi.metatavu.timebank.api.test.functional.data.TestDateUtils.Companion.getSixtyDaysAgo
import fi.metatavu.timebank.api.test.functional.data.TestDateUtils.Companion.getThirtyDaysAgo
import org.junit.jupiter.api.Assertions.assertEquals

/**
 * Tests for Synchronization API
 */
@QuarkusTest
@QuarkusTestResource.List(
    QuarkusTestResource(TestMySQLResource::class),
    QuarkusTestResource(TestWiremockResource::class)
)
@TestProfile(LocalTestProfile::class)
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
     * with mock Forecast data from past 30 and 60 days.
     */
    @Test
    fun testSynchronization() {
        createTestBuilder().use { testBuilder ->
            val amountOfPersons = testBuilder.manager.persons.getPersons().size

            testBuilder.manager.synchronization.synchronizeEntries(
                before = null,
                after = getThirtyDaysAgo().toString()
            )
            val synchronizedFirst = testBuilder.manager.timeEntries.getTimeEntries()
            val expectedFirst = daysBetweenMonth * amountOfPersons
            synchronizedFirst.forEach { testBuilder.manager.timeEntries.clean(it) }

            testBuilder.manager.synchronization.synchronizeEntries(
                before = null,
                after = getSixtyDaysAgo().toString()
            )
            val synchronizedSecond = testBuilder.manager.timeEntries.getTimeEntries()
            val expectedSecond = daysBetweenTwoMonths * amountOfPersons
            synchronizedSecond.forEach { testBuilder.manager.timeEntries.clean(it) }

            assertEquals(expectedFirst.toInt(), synchronizedFirst.size)
            assertEquals(expectedSecond.toInt(), synchronizedSecond.size)
        }
    }
}