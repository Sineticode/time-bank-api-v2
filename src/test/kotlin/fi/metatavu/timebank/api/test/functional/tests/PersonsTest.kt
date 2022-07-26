package fi.metatavu.timebank.api.test.functional.tests

import fi.metatavu.timebank.api.test.functional.data.TestData
import fi.metatavu.timebank.api.test.functional.resources.LocalTestProfile
import fi.metatavu.timebank.api.test.functional.resources.TestWiremockResource
import fi.metatavu.timebank.api.test.functional.resources.TestMySQLResource
import fi.metatavu.timebank.test.client.models.Person
import fi.metatavu.timebank.test.client.models.PersonTotalTime
import fi.metatavu.timebank.test.client.models.Timespan
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.TestProfile
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.mockito.internal.matchers.apachecommons.ReflectionEquals

/**
 * Tests for Person API
 */
@QuarkusTest
@QuarkusTestResource.List(
    QuarkusTestResource(TestMySQLResource::class),
    QuarkusTestResource(TestWiremockResource::class)
)
@TestProfile(LocalTestProfile::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonsTest: AbstractTest() {

    /**
     * Resets Wiremock scenario states before each test
     */
    @BeforeEach
    fun resetScenariosBeforeEach() {
        resetScenarios()
    }

    /**
     * Runs synchronization before tests to make sure database is populated
     */
    @BeforeAll
    fun synchronizeBeforeTests() {
        createTestBuilder().use { testBuilder ->
            testBuilder.manager.synchronization.synchronizeEntries()
        }
    }

    /**
     * Cleans persisted timeEntries after all tests
     */
    @AfterAll
    fun cleanSynchronized() {
        createTestBuilder().use { testBuilder ->
            val entries = testBuilder.manager.timeEntries.getTimeEntries()
            entries.forEach { timeEntry ->
                testBuilder.manager.timeEntries.clean(timeEntry)
            }
        }
    }

    /**
     * Tests /v1/persons?active=false -endpoint
     */
    @Test
    fun listActivePersons() {
        createTestBuilder().use { testBuilder ->
            val persons = testBuilder.manager.persons.getPersons(active = false)

            assertEquals(5, persons.size)
            testBuilder.notValid.persons.assertListFail(401)
        }
    }

    /**
     * Tests /v1/persons -endpoint
     */
     @Test
    fun listAllPersons() {
        createTestBuilder().use { testBuilder ->
            val persons = testBuilder.manager.persons.getPersons()

            assertEquals(2, persons.size)
            assertEquals(TestData.getPerson(id = 1).firstName, persons[0].firstName)
            assertEquals(29, persons[0].unspentVacations)
            assertEquals(1, persons[0].spentVacations)
            assertEquals(10, persons[0].minimumBillableRate)
            assertEquals(50, persons[1].minimumBillableRate)
            testBuilder.notValid.persons.assertListFail(401)
        }
    }

    /**
     * Tests /v1/persons/1 -endpoint PUT method
     */
    @Test
    fun updatePersons() {
        createTestBuilder().use { testBuilder ->
            val person = testBuilder.manager.persons.getPersons(active = false).find { it.id == 2 }!!
            val newPerson = Person(
                id = person.id,
                firstName = person.firstName,
                lastName = person.lastName,
                email = person.email,
                monday = person.monday,
                tuesday = person.tuesday,
                wednesday = person.wednesday,
                thursday = person.thursday,
                friday = person.friday,
                saturday = person.saturday,
                sunday = person.sunday,
                active = person.active,
                unspentVacations = person.unspentVacations,
                spentVacations = person.spentVacations,
                minimumBillableRate = 50,
                language = person.language,
                startDate = person.startDate
            )
            val updatedPerson = testBuilder.manager.persons.updatePerson(
                personId = newPerson.id,
                person = newPerson
            )

            assertEquals(25,person .minimumBillableRate)
            assertEquals(50, updatedPerson.minimumBillableRate)
            testBuilder.userA.persons.assertUpdateFail(
                person = newPerson,
                expectedStatus = 401
            )

            // Quarkus Devservices are not restarted between test runs,
            // hence resetting Keycloak attribute is necessary for consequent test runs.
            testBuilder.manager.persons.updatePerson(
                personId = person.id,
                person = person
            )
        }
    }

    /**
     * Tests /v1/persons/2/total -endpoint
     * timespan defaults to ALL_TIME
     */
    @Test
    fun listPersonTotalTimeForPersonAAllTime() {
        createTestBuilder().use { testBuilder ->
            val personTotalTimes = testBuilder.manager.persons.getPersonTotal(
                personId = TestData.getPerson(id = 2).id,
                timespan = null
            )

            assertEquals(1, personTotalTimes.size)
            assertEquals(648, personTotalTimes[0].internalTime)
            assertEquals(1200, personTotalTimes[0].projectTime)
            assertEquals(1305, personTotalTimes[0].expected)
            assertEquals(543, personTotalTimes[0].balance)
            assertEquals(1848, personTotalTimes[0].logged)
        }
    }

    /**
     * Tests /v1/persons/2/total?timespan=YEAR
     */
    @Test
    fun listPersonTotalTimeForPersonBYear() {
        createTestBuilder().use { testBuilder ->
            val personTotalTimes = testBuilder.manager.persons.getPersonTotal(
                personId = TestData.getPerson(id = 2).id,
                timespan = Timespan.YEAR
            )
            val refData = listOf(
                PersonTotalTime(
                    balance = 108,
                    logged = 1413,
                    expected = 1305,
                    internalTime = 213,
                    projectTime = 1200,
                    personId = 2,
                    timePeriod = "2022"
                ),
                PersonTotalTime(
                    balance = 435,
                    logged = 435,
                    expected = 0,
                    internalTime = 435,
                    projectTime = 0,
                    personId = 2,
                    timePeriod = "2021"
                )
            )

            assertEquals(2, personTotalTimes.size)
            refData.forEachIndexed { index, personTotalTime ->
                assertTrue(ReflectionEquals(personTotalTime, null).matches(personTotalTimes[index]))
            }
        }
    }

    /**
     * Tests /v1/persons/3/total?timespan=MONTH
     */
    @Test
    fun listPersonTotalTimeForPersonCMonth() {
        createTestBuilder().use { testBuilder ->
            val personTotalTimes = testBuilder.manager.persons.getPersonTotal(
                personId = TestData.getPerson(id = 3).id,
                timespan = Timespan.MONTH
            )
            val refData = listOf(
                PersonTotalTime(
                    balance = 122,
                    logged = 122,
                    expected = 0,
                    internalTime = 0,
                    projectTime = 122,
                    personId = 3,
                    timePeriod = "2022,4"
                ),
                PersonTotalTime(
                    balance = -433,
                    logged = 872,
                    expected = 1305,
                    internalTime = 750,
                    projectTime = 122,
                    personId = 3,
                    timePeriod = "2022,3"
                ),
                PersonTotalTime(
                    balance = -383,
                    logged = 52,
                    expected = 435,
                    internalTime = 0,
                    projectTime = 52,
                    personId = 3,
                    timePeriod = "2022,1"
                )
            )
            assertEquals(3, personTotalTimes.size)
            refData.forEachIndexed { index, personTotalTime ->
                assertTrue(ReflectionEquals(personTotalTime, null).matches(personTotalTimes[index]))
            }
        }
    }

    /**
     * Tests /v1/persons/3/total?timespan=WEEK
     */
    @Test
    fun listPersonTotalTimeForPersonCWeek() {
        createTestBuilder().use { testBuilder ->
            val personTotalTimes = testBuilder.manager.persons.getPersonTotal(
                personId = TestData.getPerson(id = 3).id,
                timespan = Timespan.WEEK
            )
            val refData = listOf(
                PersonTotalTime(
                    balance = 59,
                    logged = 494,
                    expected = 435,
                    internalTime = 372,
                    projectTime = 122,
                    personId = 3,
                    timePeriod = "2022,3,13"
                ),
                PersonTotalTime(
                    balance = -370,
                    logged = 500,
                    expected = 870,
                    internalTime = 378,
                    projectTime = 122,
                    personId = 3,
                    timePeriod = "2022,3,11"
                ),
                PersonTotalTime(
                    balance = -383,
                    logged = 52,
                    expected = 435,
                    internalTime = 0,
                    projectTime = 52,
                    personId = 3,
                    timePeriod = "2022,1,1"
                )

            )
            assertEquals(3, personTotalTimes.size)
            refData.forEachIndexed { index, personTotalTime ->
                assertTrue(ReflectionEquals(personTotalTime, null).matches(personTotalTimes[index]))
            }
        }
    }

    /**
     * Tests listing total time entries for a non-existing person
     */
    @Test
    fun listNonExistingPersonTimeEntries() {
        createTestBuilder().use { testBuilder ->
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
        createTestBuilder().use { testBuilder ->
            testBuilder.userWithNullToken.persons.assertListFailWithNullToken(expectedStatus = 401)
        }
    }

    /**
     * Tests /v1/persons -endpoint when Forecast API persons -endpoint responses with an error
     */
     @Test
    fun testPersonsWithForecastError() {
        setScenario(
            scenario = PERSONS_SCENARIO,
            state = ERROR_STATE
        )
        createTestBuilder().use { testBuilder ->
            testBuilder.manager.persons.assertListFail(expectedStatus = 400)
        }
    }
}