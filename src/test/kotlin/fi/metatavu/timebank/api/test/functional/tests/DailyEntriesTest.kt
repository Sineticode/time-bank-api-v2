package fi.metatavu.timebank.api.test.functional.tests

import fi.metatavu.timebank.api.test.functional.TestBuilder
import fi.metatavu.timebank.api.test.functional.resources.LocalTestProfile
import fi.metatavu.timebank.api.test.functional.resources.TestMySQLResource
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.TestProfile
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

/**
 * Tests for Daily Entries API
 */
@QuarkusTest
@QuarkusTestResource.List(
    QuarkusTestResource(TestMySQLResource::class)
)
@TestProfile(LocalTestProfile::class)
class DailyEntriesTest {

    /**
     * Tests /v1/dailyEntries -endpoint
     */
    @Test
    fun testDailyEntries() {
        TestBuilder().use {
            it.manager.synchronization.synchronizeEntries()

            val dailyEntries = it.manager.dailyEntries.getDailyEntries()

            assertEquals(2, dailyEntries.size)
        }
    }
  }