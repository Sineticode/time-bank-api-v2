package fi.metatavu.timebank.api.test.functional.tests

import fi.metatavu.timebank.api.test.functional.TestBuilder
import fi.metatavu.timebank.api.test.functional.resources.LocalTestProfile
import fi.metatavu.timebank.api.test.functional.resources.TestMockResource
import fi.metatavu.timebank.api.test.functional.resources.TestMySQLResource
import fi.metatavu.timebank.test.client.models.DailyEntry
import fi.metatavu.timebank.test.client.models.PersonTotalTime
import fi.metatavu.timebank.test.client.models.Timespan
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.TestProfile
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.mockito.internal.matchers.apachecommons.ReflectionEquals
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.WeekFields

/**
 * Tests for Person API
 */
@QuarkusTest
@QuarkusTestResource.List(
    QuarkusTestResource(TestMySQLResource::class),
    QuarkusTestResource(TestMockResource::class)
)
@TestProfile(LocalTestProfile::class)
@TestClassOrder(ClassOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Order(3)
class PersonsTest: AbstractTest() {

    /**
     * Runs synchronization to make sure that test database is populated before all tests.
     */
    @BeforeAll
    fun runSynchronizationBeforeTests() {
        resetScenarios()
        TestBuilder().use { testBuilder ->
            testBuilder.manager.synchronization.synchronizeEntries()
        }
    }

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
            assertEquals(TestData.getPerson(id = 1).first_name, persons[0].firstName)
            testBuilder.notvalid.persons.assertListFail(401)
        }
    }

    /**
     * Tests /v1/persons/1/total
     *  timespan defaults to ALL_TIME
     */
     @Test
    fun listPersonTotalTimeForPersonA() {
        TestBuilder().use { testBuilder ->
            val personTotalTimes = testBuilder.manager.persons.getPersonTotal(
                personId = TestData.getPerson(id = 1).id,
                timespan = null
            )
            val personAEntries = testBuilder.manager.dailyEntries.getDailyEntries(
                personId = 1,
                before = null,
                after = null
            ).groupBy { LocalDate.parse(it.date).year }.values
            val personARefData = personAEntries.map { days ->
                calculatePersonTotalTimes(
                    personId = 1,
                    days = days,
                    timespan = Timespan.aLLTIME
                )
            }.first()

            assertEquals(1, personTotalTimes.size)
            assertTrue(ReflectionEquals(personARefData, null).matches(personTotalTimes.first()))
        }
    }

    /**
     * Tests /v1/persons/2/total?timespan=YEAR
     */
     @Test
    fun listPersonTotalTimeForPersonB() {
        TestBuilder().use { testBuilder ->
            val personTotalTimes = testBuilder.manager.persons.getPersonTotal(
                personId = TestData.getPerson(id = 2).id,
                timespan = Timespan.yEAR
            )
            val personBEntries = testBuilder.manager.dailyEntries.getDailyEntries(
                personId = 2,
                before = null,
                after = null
            ).groupBy { LocalDate.parse(it.date).year }.values
            val personBRefData = personBEntries.map { days ->
                calculatePersonTotalTimes(
                    personId = 2,
                    days = days,
                    timespan = Timespan.yEAR
                )
            }

            personBRefData.forEachIndexed { index, _ ->
                personTotalTimes.forEach { actual ->
                    assertTrue(ReflectionEquals(personBRefData[index], null).matches(personTotalTimes[index]))
                }
            }
        }
    }

    /**
     * Tests /v1/persons/3/total?timespan=MONTH
     */
     @Test
    fun listPersonTotalTimeForPersonC() {
        TestBuilder().use { testBuilder ->
            val personTotalTimes = testBuilder.manager.persons.getPersonTotal(
                personId = TestData.getPerson(id = 3).id,
                timespan = Timespan.mONTH
            )
            val personCEntries = testBuilder.manager.dailyEntries.getDailyEntries(
                personId = 3,
                before = null,
                after = null
            ).groupBy { Pair(LocalDate.parse(it.date).year, LocalDate.parse(it.date).monthValue) }.values
            val personCRefData = personCEntries.map { days ->
                calculatePersonTotalTimes(
                    personId = 3,
                    days = days,
                    timespan = Timespan.mONTH
                )
            }

            assertEquals(personCRefData.size, personTotalTimes.size)
            personCRefData.forEachIndexed { index, expected ->
                personTotalTimes.forEach { actual ->
                    assertTrue(ReflectionEquals(personCRefData[index], null).matches(personTotalTimes[index]))
                }
            }
        }
    }

    /**
     * Tests /v1/persons/3/total?timespan=WEEK
     */
     @Test
    fun listPersonTotalTimeForPersonCWeek() {
        val weekOfYear = WeekFields.of(DayOfWeek.MONDAY, 7).weekOfYear()
        TestBuilder().use { testBuilder ->
            val personTotalTimes = testBuilder.manager.persons.getPersonTotal(
                personId = TestData.getPerson(id = 3).id,
                timespan = Timespan.wEEK
            ).sortedBy { it.timePeriod!!.split(",")[2] }
            val personCEntries = testBuilder.manager.dailyEntries.getDailyEntries(
                personId = 3,
                before = null,
                after = null
            ).groupBy { Pair(LocalDate.parse(it.date).year, LocalDate.parse(it.date).get(weekOfYear)) }
                .values
            val personCRefData = personCEntries.map { days ->
                calculatePersonTotalTimes(
                    personId = 3,
                    days = days,
                    timespan = Timespan.wEEK
                )
            }.sortedBy { it.timePeriod!!.split(",")[2] }

            assertEquals(personCRefData.size, personTotalTimes.size)
            personCRefData.forEachIndexed { index, expected ->
                personTotalTimes.forEach { actual ->
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
            testBuilder.userWithNullToken.persons.assertListFailWithNullToken(
                expectedStatus = 401
            )
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
            testBuilder.manager.persons.assertListFail(400)
        }
    }

    private fun calculatePersonTotalTimes(personId: Int, days: List<DailyEntry>, timespan: Timespan): PersonTotalTime {
        val weekOfYear = WeekFields.of(DayOfWeek.MONDAY, 7).weekOfYear()
        var internalTime = 0
        var projectTime = 0
        var expected = 0
        var year: Int? = null
        var month: Int? = null
        var week: Int? = null
        var startDate: LocalDate? = null
        var endDate: LocalDate? = null

        days.forEachIndexed { index, dailyEntry ->
            internalTime += dailyEntry.internalTime
            projectTime += dailyEntry.projectTime
            expected += dailyEntry.expected
            year = if (timespan != Timespan.aLLTIME) LocalDate.parse(dailyEntry.date).year else null
            month = if (timespan == Timespan.mONTH || timespan == Timespan.wEEK) LocalDate.parse(dailyEntry.date).monthValue else null
            week = if (timespan == Timespan.wEEK) LocalDate.parse(dailyEntry.date).get(weekOfYear) else null
            if (index == 0) endDate = LocalDate.parse(dailyEntry.date)
            if (index == days.lastIndex) startDate = LocalDate.parse(dailyEntry.date)
        }

        val timePeriod = timespanDateStringBuilder(timespan, year, month, week, startDate, endDate)

        return PersonTotalTime(
            balance = internalTime + projectTime - expected,
            logged = internalTime + projectTime,
            expected = expected,
            internalTime = internalTime,
            projectTime = projectTime,
            personId = personId,
            timePeriod = timePeriod
        )
    }
    private fun timespanDateStringBuilder(timespan: Timespan, year: Int?, month: Int?, week: Int?, startDate: LocalDate?, endDate: LocalDate?): String {
        return when (timespan) {
            Timespan.aLLTIME -> "$startDate,$endDate"
            Timespan.yEAR -> "$year"
            Timespan.mONTH -> "$year,$month"
            Timespan.wEEK -> "$year,$month,$week"
        }
    }
}