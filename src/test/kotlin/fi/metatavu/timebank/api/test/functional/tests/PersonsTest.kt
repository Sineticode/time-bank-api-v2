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
 * Tests for Person API
 */
@QuarkusTest
@QuarkusTestResource.List(
    QuarkusTestResource(TestMySQLResource::class),
)
@TestProfile(LocalTestProfile::class)
@TestClassOrder(ClassOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Order(3)
class PersonsTest {

    /**
     * Runs synchronization to make sure that test database is populated before all tests.
     */
    @BeforeAll
    fun runSynchronizationBeforeTests() {
        TestBuilder().use { testBuilder ->
            testBuilder.manager.synchronization.synchronizeEntries()
        }
    }

    /**
     * Tests /v1/persons?active=false -endpoint
     */
    @Test
    fun listActivePersons() {

        TestBuilder().use { testBuilder ->

//            testBuilder.manager.synchronization.synchronizeEntries()

            val persons = testBuilder.manager.persons.getPersons()

            assertEquals(1, persons.size)

            testBuilder.notvalid.persons.assertListFail(401)
        }
    }

    /**
     * Tests /v1/persons -endpoint
     */
    @Test
    fun listAllPersons() {
        TestBuilder().use { testBuilder ->

            val persons = testBuilder.manager.persons.getPersons(
                active = false
            )

            assertEquals(3, persons.size)
            assertEquals(TestData.getPersonA().first_name, persons[0].firstName)

            testBuilder.notvalid.persons.assertListFail(401)
        }
    }

    /**
     * Tests /v1/persons/1/total
     */
    @Test
    fun listPersonTotalTimeForPersonA() {
        TestBuilder().use { testBuilder ->

            val personTotalTimes = testBuilder.manager.persons.getPersonTotal(
                personId = TestData.getPersonA().id,
                timespan = null
            )

            assertEquals(1, personTotalTimes.size)
        }
    }
}