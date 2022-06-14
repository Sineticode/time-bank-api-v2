package fi.metatavu.timebank.api.test.functional.tests

import fi.metatavu.timebank.api.forecast.models.ForecastHoliday
import fi.metatavu.timebank.api.forecast.models.ForecastPerson
import fi.metatavu.timebank.api.forecast.models.ForecastTimeEntry
import fi.metatavu.timebank.api.forecast.models.ForecastTimeEntryResponse
import fi.metatavu.timebank.api.test.functional.data.TestPersonsData
import fi.metatavu.timebank.api.test.functional.data.TestTimeEntriesData
import java.time.LocalDate

/**
 * Class used by Wiremock to retrieve mock test data
 */
class TestData {

    companion object {
        fun getForecastTimeEntryResponse(before: String? = null, after: String? = null): ForecastTimeEntryResponse {
            return if (after != null) {
                val pageContents = getForecastTimeEntry().filter { forecastTimeEntry ->
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
            } else if (before != null) {
                val pageContents = getForecastTimeEntry().filter { forecastTimeEntry ->
                    LocalDate.parse(forecastTimeEntry.date) < LocalDate.parse(before)
                }
                ForecastTimeEntryResponse(
                    pageContents = pageContents,
                    pageNumber = 1,
                    pageSize = pageContents.size,
                    totalObjectCount = pageContents.size,
                    status = 200,
                    message = null
                )
        } else ForecastTimeEntryResponse(
                pageContents = getForecastTimeEntry(),
                pageNumber = 1,
                pageSize = getForecastTimeEntry().size,
                totalObjectCount = getForecastTimeEntry().size,
                status = 200,
                message = null
            )
        }

        fun getUpdatedForecastTimeEntryResponse(): ForecastTimeEntryResponse {
            return ForecastTimeEntryResponse(
                pageContents = TestTimeEntriesData.getUpdatedForecastTimeEntry(),
                pageNumber = 1,
                pageSize = 1,
                totalObjectCount = 1,
                status = 200,
                message = null
            )
        }

        fun getGeneratedForecastTimeEntryResponse(pageNumber: Int): ForecastTimeEntryResponse {
            return ForecastTimeEntryResponse(
                pageContents = TestTimeEntriesData.generateRandomForecastTimeEntries(pageNumber),
                pageNumber = pageNumber,
                pageSize = 1000,
                totalObjectCount = 2000,
                status = 200,
                message = null
            )
        }

        fun getForecastTimeEntry(id: Int? = null, person: Int? = null): List<ForecastTimeEntry> {
            return if (id != null) {
                TestTimeEntriesData.getForecastTimeEntries().filter { it.id == id }
            } else if (person != null) {
                TestTimeEntriesData.getForecastTimeEntries().filter { it.person == person }
            } else {
                TestTimeEntriesData.getForecastTimeEntries()
            }
        }

        fun getPerson(id: Int): ForecastPerson {
            return TestPersonsData.getPersons().filter { it.id == id }[0]
        }

        fun getPersons(): List<ForecastPerson> {
            return TestPersonsData.getPersons()
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