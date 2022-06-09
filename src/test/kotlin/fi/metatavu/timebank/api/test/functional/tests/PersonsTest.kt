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
            val personAEntries = TestData.forecastTimeEntries.filter { entry -> entry.person == TestData.getPersonA().id }
            var projectTime = 0
            var internalTime = 0
            var totalTimeRegistered = 0

            personAEntries.forEach { forecastTimeEntry ->
                totalTimeRegistered += forecastTimeEntry.time_registered
                if (forecastTimeEntry.non_project_time == null) {
                    projectTime += forecastTimeEntry.time_registered
                } else {
                    internalTime += forecastTimeEntry.time_registered
                }
            }

            assertEquals(1, personTotalTimes.size)
            assertEquals(totalTimeRegistered, personTotalTimes[0].logged)
            assertEquals(internalTime, personTotalTimes[0].internalTime)
            assertEquals(projectTime, personTotalTimes[0].projectTime)
        }
    }

    /**
     * Tests listing total time entries for a non-existing person
     */
    @Test
    fun listNonExistingPersonTimeEntries() {
        TestBuilder().use { testBuilder ->
            testBuilder.manager.persons.assertTotalsFail(
                expectedStatus = 404,
                personId = 123
            )
        }
    }

    /**
     * Test /v1/persons?active=false without access token
     */
    @Test
    fun listPersonsWithNullToken() {
        TestBuilder().use { testBuilder ->
            testBuilder.userWithNullToken.persons.assertListFailWithNullToken(
                expectedStatus = 401
            )
        }
    }
}