package fi.metatavu.timebank.api.tests

import fi.metatavu.timebank.model.DailyEntry
import fi.metatavu.timebank.model.Person
import fi.metatavu.timebank.model.PersonTotalTime
import java.time.LocalDate

/**
 * Class for test data that is used by wiremock and tests
 */
class TestData {
    companion object{

        fun getPersonA(): Person {
            return Person(
                id = 395952,
                firstName = "Tester",
                lastName = "Test",
                monday = 435,
                tuesday = 435,
                wednesday = 435,
                thursday = 435,
                friday = 435,
                saturday = 0,
                sunday = 0,
                active = true,
                startDate = "2022-05-11",
                defaultRole = null
            )
        }

        fun getDailyEntryA(): DailyEntry {
            return DailyEntry(
                person = 395952,
                internalTime = 56,
                projectTime = 144,
                logged = 200,
                expected = 200,
                balance = 200,
                date = LocalDate.now()
            )
        }
        fun getTotalTimespanWeek(): PersonTotalTime {
            return PersonTotalTime(
                balance = 150,
                logged = 250,
                internalTime = 100,
                projectTime = 150,
                expected =  500,
                personId = 395952,
                year = 2022,
                monthNumber = 5,
                weekNumber = 22
            )
        }
        fun getTotalTimespanMonth(): PersonTotalTime {
            return PersonTotalTime(
                balance = 150,
                logged = 250,
                internalTime = 100,
                projectTime = 150,
                expected =  500,
                personId = 395952,
                year = 2022,
                monthNumber = 5,
                weekNumber = null
            )
        }
        fun getTotalTimespanYear(): PersonTotalTime {
            return PersonTotalTime(
                balance = 150,
                logged = 250,
                internalTime = 100,
                projectTime = 150,
                expected =  500,
                personId = 395952,
                year = 2022,
                monthNumber = null,
                weekNumber = null
            )
        }
    }
}