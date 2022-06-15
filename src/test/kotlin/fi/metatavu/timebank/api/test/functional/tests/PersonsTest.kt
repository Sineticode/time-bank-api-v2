package fi.metatavu.timebank.api.test.functional.tests

import fi.metatavu.timebank.api.controllers.PersonsController
import fi.metatavu.timebank.api.test.functional.TestBuilder
import fi.metatavu.timebank.api.test.functional.data.TestData
import fi.metatavu.timebank.api.test.functional.resources.LocalTestProfile
import fi.metatavu.timebank.api.test.functional.resources.TestWiremockResource
import fi.metatavu.timebank.api.test.functional.resources.TestMySQLResource
import fi.metatavu.timebank.test.client.models.Timespan
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.TestProfile
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.mockito.internal.matchers.apachecommons.ReflectionEquals
import javax.inject.Inject

/**
 * Tests for Person API
 */
@QuarkusTest
@QuarkusTestResource.List(
    QuarkusTestResource(TestMySQLResource::class),
    QuarkusTestResource(TestWiremockResource::class)
)
@TestProfile(LocalTestProfile::class)
@TestClassOrder(ClassOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Order(2)
class PersonsTest: AbstractTest() {

    @Inject
    lateinit var personsController: PersonsController

    /**
     * Resets Wiremock scenario states before each test
     */
    @BeforeEach
    fun resetScenariosBeforeEach() {
        resetScenarios()
    }

    /**
     * Tests /v1/persons?active=false -endpoint
     */
     @Test
    fun listActivePersons() {
        TestBuilder().use { testBuilder ->
            val persons = testBuilder.manager.persons.getPersons(active = false)

            assertEquals(4, persons.size)
            testBuilder.notValid.persons.assertListFail(401)
        }
    }

    /**
     * Tests /v1/persons -endpoint
     */
     @Test
    fun listAllPersons() {
        TestBuilder().use { testBuilder ->
            val persons = testBuilder.manager.persons.getPersons()

            assertEquals(1, persons.size)
            assertEquals(TestData.getPerson(id = 1).first_name, persons[0].firstName)
            testBuilder.notValid.persons.assertListFail(401)
        }
    }

    /**
     * Tests /v1/persons/1/total
     *  timespan defaults to ALL_TIME
     */
     @Test
    suspend fun listPersonTotalTimeForPersonAAllTime() {
        TestBuilder().use { testBuilder ->
            val personTotalTimes = testBuilder.manager.persons.getPersonTotal(
                personId = TestData.getPerson(id = 1).id,
                timespan = null
            )
            val personARefData = personsController.makePersonTotal(personId = 1, timespan = fi.metatavu.timebank.model.Timespan.ALL_TIME)

            assertEquals(1, personTotalTimes.size)
            assertTrue(ReflectionEquals(personARefData!!.first(), null).matches(personTotalTimes.first()))
        }
    }

    /**
     * Tests /v1/persons/2/total?timespan=YEAR
     */
     @Test
    suspend fun listPersonTotalTimeForPersonBYear() {
        TestBuilder().use { testBuilder ->
            val personTotalTimes = testBuilder.manager.persons.getPersonTotal(
                personId = TestData.getPerson(id = 2).id,
                timespan = Timespan.yEAR
            )
            val personBRefData = personsController.makePersonTotal(personId = 2, timespan = fi.metatavu.timebank.model.Timespan.YEAR)

            personBRefData!!.forEachIndexed { index, _ ->
                personTotalTimes.forEach { _ ->
                    assertTrue(ReflectionEquals(personBRefData[index], null).matches(personTotalTimes[index]))
                }
            }
        }
    }

    /**
     * Tests /v1/persons/3/total?timespan=MONTH
     */
     @Test
    suspend fun listPersonTotalTimeForPersonCMonth() {
        TestBuilder().use { testBuilder ->
            val personTotalTimes = testBuilder.manager.persons.getPersonTotal(
                personId = TestData.getPerson(id = 3).id,
                timespan = Timespan.mONTH
            )
            val personCRefData = personsController.makePersonTotal(personId = 3, timespan = fi.metatavu.timebank.model.Timespan.MONTH)

            assertEquals(personCRefData!!.size, personTotalTimes.size)
            personCRefData.forEachIndexed { index, _ ->
                personTotalTimes.forEach { _ ->
                    assertTrue(ReflectionEquals(personCRefData[index], null).matches(personTotalTimes[index]))
                }
            }
        }
    }

    /**
     * Tests /v1/persons/3/total?timespan=WEEK
     */
     @Test
    suspend fun listPersonTotalTimeForPersonCWeek() {
        TestBuilder().use { testBuilder ->
            val personTotalTimes = testBuilder.manager.persons.getPersonTotal(
                personId = TestData.getPerson(id = 3).id,
                timespan = Timespan.wEEK
            )
            val personCRefData = personsController.makePersonTotal(personId = 3, timespan = fi.metatavu.timebank.model.Timespan.WEEK)

            assertEquals(personCRefData!!.size, personTotalTimes.size)
            personCRefData.forEachIndexed { index, _ ->
                personTotalTimes.forEach { _ ->
                    assertTrue(ReflectionEquals(personCRefData[index], null).matches(personTotalTimes[index]))
                }
            }
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
        TestBuilder().use { testBuilder ->
            testBuilder.manager.persons.assertListFail(expectedStatus = 400)
        }
    }
}