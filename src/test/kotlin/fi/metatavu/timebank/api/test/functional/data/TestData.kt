package fi.metatavu.timebank.api.test.functional.data

import fi.metatavu.timebank.api.forecast.models.ForecastHoliday
import fi.metatavu.timebank.api.forecast.models.ForecastPerson
import fi.metatavu.timebank.api.forecast.models.ForecastTimeEntryResponse
import java.time.LocalDate

/**
 * Class used by Wiremock to retrieve mock test data
 */
class TestData {

    companion object {

        /**
         * Gets mock Forecast API response to /v4/time_registrations
         *
         * @param before before LocalDate as string to filter time entries
         * @param after after LocalDate as string to filter time entries
         * @return ForecastTimeEntryResponse
         */
        fun getForecastTimeEntryResponse(before: String? = null, after: String? = null): ForecastTimeEntryResponse {
            return if (after != null) {
                val pageContents = TestTimeEntriesData.getForecastTimeEntries().filter { forecastTimeEntry ->
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
                val pageContents = TestTimeEntriesData.getForecastTimeEntries().filter { forecastTimeEntry ->
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
                pageContents = TestTimeEntriesData.getForecastTimeEntries(),
                pageNumber = 1,
                pageSize = TestTimeEntriesData.getForecastTimeEntries().size,
                totalObjectCount = TestTimeEntriesData.getForecastTimeEntries().size,
                status = 200,
                message = null
            )
        }

        /**
         * Gets mock Forecast API response to /v4/time_registrations
         * with time entries with updated values
         *
         * @return ForecastTimeEntryResponse
          */
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

        /**
         * Gets mock Forecast API response to /v4/time_registrations
         * with 2000 random generated time entries
         *
         * @param pageNumber Integer 1 or 2
         * @return ForecastTimeEntryResponse
         */
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

        /**
         * Gets one mock person by id
         *
         * @param id personId
         * @return ForecastPerson
         */
        fun getPerson(id: Int): ForecastPerson {
            return TestPersonsData.getPersons().filter { it.id == id }[0]
        }

        /**
         * Gets all mock persons
         *
         * @return List of ForecastPerson
         */
        fun getPersons(): List<ForecastPerson> {
            return TestPersonsData.getPersons()
        }

        /**
         * Gets mock ForecastHolidays
         *
         * @return List of ForecastHolidays
         */
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