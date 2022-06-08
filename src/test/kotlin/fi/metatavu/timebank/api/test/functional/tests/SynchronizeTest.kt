package fi.metatavu.timebank.api.test.functional.tests

import fi.metatavu.timebank.api.test.functional.TestBuilder
import fi.metatavu.timebank.api.test.functional.resources.LocalTestProfile
import fi.metatavu.timebank.api.test.functional.resources.TestMockResource
import fi.metatavu.timebank.api.test.functional.resources.TestMySQLResource
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.TestProfile
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.*

/**
 * Test for Synchronization API
 */
@QuarkusTest
@QuarkusTestResource.List(
    QuarkusTestResource(TestMockResource::class),
    QuarkusTestResource(TestMySQLResource::class)
)
@TestProfile(LocalTestProfile::class)
class SynchronizeTest {

    /**
     * Tests /v1/synchronize -endpoint
     */
    @Test
    fun testSynchronization() {
        TestBuilder().use {
            val synchronized = it.manager.synchronization.synchronizeEntries()

            assertEquals(3, synchronized.message)
        }
    }
}