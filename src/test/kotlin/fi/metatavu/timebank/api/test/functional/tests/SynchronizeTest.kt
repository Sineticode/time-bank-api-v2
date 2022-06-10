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
     * Tests /v1/synchronize -endpoint
     */
    @Test
    fun testSynchronization() {
        TestBuilder().use { testBuilder ->
            val synchronizedAfter = testBuilder.manager.synchronization.synchronizeEntries(
                before = null,
                after = "2022-05-01"
            )
            resetScenarios()

            val synchronized = testBuilder.manager.synchronization.synchronizeEntries()

            assertEquals(3, synchronizedAfter.message)
            assertEquals(6, synchronized.message)
        }
    }

    /**
     *  Tests /v1/synchronize -endpoint when Forecast API responses with an error
     */
    @Test
    fun testSynchronizationForecastError() {
        TestBuilder().use { testBuilder ->
            testBuilder.manager.synchronization.assertSynchronizeFail(400)

        }
    }
}