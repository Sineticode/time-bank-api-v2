package fi.metatavu.timebank.api.test.functional.tests

import fi.metatavu.timebank.api.test.functional.TestBuilder
import fi.metatavu.timebank.api.test.functional.resources.LocalTestProfile
import fi.metatavu.timebank.api.test.functional.resources.TestMockResource
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
    QuarkusTestResource(TestMockResource::class),
)
@TestProfile(LocalTestProfile::class)
class PersonsTest {

    /**
     * Tests /v1/persons?active=false -endpoint
     */
    @Test
    fun listPersons() {
        TestBuilder().use {
            val persons = it.manager.persons.getPersons()

            assertEquals(3, persons.size)

            it.notvalid.persons.assertListFail(401)
        }
    }

    /**
     * Tests /v1/persons -endpoint
     */
    @Test
    fun listActivePersons() {
        TestBuilder().use {
            val activePersons = it.manager.persons.getActivePersons()

            assertEquals(1, activePersons.size)
            assertEquals(TestData.getPersonA().first_name, activePersons[0].firstName)

            it.notvalid.persons.assertListFail(401)
        }
    }
}