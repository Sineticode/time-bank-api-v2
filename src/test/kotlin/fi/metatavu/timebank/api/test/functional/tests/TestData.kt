package fi.metatavu.timebank.api.test.functional.tests

import fi.metatavu.timebank.api.forecast.models.ForecastHoliday
import fi.metatavu.timebank.api.forecast.models.ForecastPerson
import fi.metatavu.timebank.api.forecast.models.ForecastTimeEntry
import fi.metatavu.timebank.api.forecast.models.ForecastTimeEntryResponse
import java.time.LocalDate

/**
 * Class for test data that is used by wiremock and tests
 */
class TestData {
    companion object{

        fun getForecastTimeEntryResponse(after: String? = null): ForecastTimeEntryResponse {
            return if (after  != null) {
                val pageContents = forecastTimeEntries.filter { forecastTimeEntry ->
                    LocalDate.parse(forecastTimeEntry.date) > LocalDate.parse(after)
                }
                ForecastTimeEntryResponse(
                    pageContents = pageContents,
                    pageNumber = 1,
                    pageSize = pageContents.size,
                    totalObjectCount = pageContents.size,
                    status = 200,
                    message = null
                )
            }
             else ForecastTimeEntryResponse(
                pageContents = forecastTimeEntries,
                pageNumber = 1,
                pageSize = forecastTimeEntries.size,
                totalObjectCount = forecastTimeEntries.size,
                status = 200,
                message = null
            )
        }
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
                id = 2,
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
                id = 3,
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

        private val forecastTimeEntries = listOf(
            ForecastTimeEntry(
                id = 1,
                person = 1,
                project = null,
                card = null,
                task = null,
                non_project_time = 255455,
                time_registered = 100,
                date = "2022-05-30",
                notes = null,
                approval_status = null,
                created_by = 1,
                updated_by = 1,
                created_at = "2022-05-30T04:53:33Z",
                updated_at = "2022-05-30T04:53:33Z",
                phase = null,
                task_project = null,
                invoice_entry = null,
                invoice = null
            ),
            ForecastTimeEntry(
                id = 2,
                person = 1,
                project = null,
                card = null,
                task = null,
                non_project_time = null,
                time_registered = 100,
                date = "2022-05-30",
                notes = null,
                approval_status = null,
                created_by = 1,
                updated_by = 1,
                created_at = "2022-05-30T04:53:33Z",
                updated_at = "2022-05-30T04:53:33Z",
                phase = null,
                task_project = null,
                invoice_entry = null,
                invoice = null
            ),
            ForecastTimeEntry(
                id = 3,
                person = 2,
                project = null,
                card = null,
                task = null,
                non_project_time = null,
                time_registered = 400,
                date = "2022-05-30",
                notes = null,
                approval_status = null,
                created_by = 1,
                updated_by = 1,
                created_at = "2022-05-30T04:53:33Z",
                updated_at = "2022-05-30T04:53:33Z",
                phase = null,
                task_project = null,
                invoice_entry = null,
                invoice = null
            ),
            ForecastTimeEntry(
                id = 4,
                person = 2,
                project = null,
                card = null,
                task = null,
                non_project_time = null,
                time_registered = 400,
                date = "2022-04-30",
                notes = null,
                approval_status = null,
                created_by = 1,
                updated_by = 1,
                created_at = "2022-05-30T04:53:33Z",
                updated_at = "2022-05-30T04:53:33Z",
                phase = null,
                task_project = null,
                invoice_entry = null,
                invoice = null
            )
        )

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