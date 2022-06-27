package fi.metatavu.timebank.api.test.functional.tests

import fi.metatavu.timebank.api.test.functional.TestBuilder
import fi.metatavu.timebank.api.test.functional.data.TestData
import fi.metatavu.timebank.api.test.functional.resources.LocalTestProfile
import fi.metatavu.timebank.api.test.functional.resources.TestWiremockResource
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