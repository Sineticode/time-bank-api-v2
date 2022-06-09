package fi.metatavu.timebank.api.test.functional.tests

import fi.metatavu.timebank.api.test.functional.TestBuilder
import fi.metatavu.timebank.api.test.functional.resources.LocalTestProfile
import fi.metatavu.timebank.api.test.functional.resources.TestMySQLResource
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.TestProfile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.ClassOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestClassOrder

/**
 * Test for Synchronization API
 */
@QuarkusTest
@QuarkusTestResource.List(
    QuarkusTestResource(TestMySQLResource::class)
)
@TestProfile(LocalTestProfile::class)
@TestClassOrder(ClassOrderer.OrderAnnotation::class)
@Order(1)
class SynchronizeTest {

    /**
     * Tests /v1/synchronize -endpoint
     */
    @Test
    fun testSynchronization() {
        TestBuilder().use { testBuilder ->
            val synchronized = testBuilder.manager.synchronization.synchronizeEntries()

            assertEquals(4, synchronized.message)
        }
    }
}