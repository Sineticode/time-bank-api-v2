package fi.metatavu.timebank.api.test.functional.tests

import fi.metatavu.timebank.api.forecast.models.ForecastHoliday
import fi.metatavu.timebank.api.forecast.models.ForecastPerson
import fi.metatavu.timebank.model.DailyEntry
import fi.metatavu.timebank.model.PersonTotalTime
import java.time.LocalDate

/**
 * Class for test data that is used by wiremock and tests
 */
class TestData {
    companion object{

        fun getPersons(): Array<ForecastPerson> {
            return arrayOf(getPersonA(), getPersonB(), getPersonC())
        }

        fun getPersonA(): ForecastPerson {
            return ForecastPerson(
                id = 1,
                first_name = "Tester",
                last_name = "Test",
                email = null,
                monday = 435,
                tuesday = 435,
                wednesday = 435,
                thursday = 435,
                friday = 435,
                saturday = 0,
                sunday = 0,
                active = true,
                default_role = null,
                cost = 0.0,
                language = "ENGLISH_UK",
                created_by = null,
                updated_by = null,
                client_id = null,
                holiday_calendar_id = 123456,
                start_date = "2022-05-11",
                end_date = null,
                created_at = null,
                updated_at = null,
                department_id = null,
                permissions = listOf("Test"),
                is_system_user = false
            )
        }

        fun getPersonB(): ForecastPerson {
            return ForecastPerson(
                id = 1,
                first_name = "Tester2",
                last_name = "Test2",
                email = null,
                monday = 435,
                tuesday = 435,
                wednesday = 435,
                thursday = 435,
                friday = 435,
                saturday = 0,
                sunday = 0,
                active = false,
                default_role = null,
                cost = 0.0,
                language = "ENGLISH_UK",
                created_by = null,
                updated_by = null,
                client_id = null,
                holiday_calendar_id = 123456,
                start_date = "2022-05-05",
                end_date = null,
                created_at = null,
                updated_at = null,
                department_id = null,
                permissions = listOf("Test"),
                is_system_user = false
            )
        }

        fun getPersonC(): ForecastPerson {
            return ForecastPerson(
                id = 1,
                first_name = "Tester3",
                last_name = "Test3",
                email = null,
                monday = 435,
                tuesday = 435,
                wednesday = 435,
                thursday = 435,
                friday = 435,
                saturday = 0,
                sunday = 0,
                active = false,
                default_role = null,
                cost = 0.0,
                language = "ENGLISH_UK",
                created_by = null,
                updated_by = null,
                client_id = null,
                holiday_calendar_id = 123456,
                start_date = "2022-05-11",
                end_date = null,
                created_at = null,
                updated_at = null,
                department_id = null,
                permissions = listOf("Test"),
                is_system_user = false
            )
        }

        fun getDailyEntryA(): DailyEntry {
            return DailyEntry(
                person = 1,
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
                timePeriod = "this week of the year..."
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
                timePeriod = "this week of the year..."

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
                timePeriod = "this week of the year..."
            )
        }

        fun getHolidays(): List<ForecastHoliday> {
            return listOf(
                ForecastHoliday(
                id = 1,
                holiday_calendar_id = 2,
                year = 2022,
                month = 6,
                day = 5,
                name = "Helluntai",
                created_by = 0,
                updated_by = 0,
                created_at = "2022-05-01",
                updated_at = "2022-05-01"
            ))
        }
    }
}