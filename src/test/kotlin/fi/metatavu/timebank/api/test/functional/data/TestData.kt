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
            val forecastResponse = ForecastTimeEntryResponse()
            return if (after != null) {
                val pageContents = TestTimeEntriesData.getForecastTimeEntries().filter { forecastTimeEntry ->
                    LocalDate.parse(forecastTimeEntry.date) > LocalDate.parse(after)
                }
                forecastResponse.pageContents = pageContents
                forecastResponse.pageSize = pageContents.size
                forecastResponse.totalObjectCount = pageContents.size

                forecastResponse
            } else if (before != null) {
                val pageContents = TestTimeEntriesData.getForecastTimeEntries().filter { forecastTimeEntry ->
                    LocalDate.parse(forecastTimeEntry.date) < LocalDate.parse(before)
                }
                forecastResponse.pageContents = pageContents
                forecastResponse.pageSize = pageContents.size
                forecastResponse.totalObjectCount = pageContents.size

                forecastResponse
            } else {
                forecastResponse.pageContents = TestTimeEntriesData.getForecastTimeEntries()
                forecastResponse.pageSize = TestTimeEntriesData.getForecastTimeEntries().size
                forecastResponse.totalObjectCount = TestTimeEntriesData.getForecastTimeEntries().size

                forecastResponse
            }
        }

        /**
         * Gets mock Forecast API response to /v4/time_registrations
         * with time entries with updated values
         *
         * @return ForecastTimeEntryResponse
         */
        fun getUpdatedForecastTimeEntryResponse(): ForecastTimeEntryResponse {
            val forecastResponse = ForecastTimeEntryResponse()
            forecastResponse.pageContents = TestTimeEntriesData.getUpdatedForecastTimeEntry()
            forecastResponse.pageSize = 1
            forecastResponse.totalObjectCount = 1

            return forecastResponse
        }

        /**
         * Gets mock Forecast API response to /v4/time_registrations
         * with 2000 random generated time entries
         *
         * @param pageNumber Integer 1 or 2
         * @return ForecastTimeEntryResponse
         */
        fun getGeneratedForecastTimeEntryResponse(pageNumber: Int): ForecastTimeEntryResponse {
            val forecastResponse = ForecastTimeEntryResponse()
            forecastResponse.pageContents = TestTimeEntriesData.generateRandomForecastTimeEntries(pageNumber)
            forecastResponse.pageSize = 1000
            forecastResponse.totalObjectCount = 1200

            return forecastResponse
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
            val holidayA = ForecastHoliday()
            holidayA.id = 1
            holidayA.holidayCalendarId = 2
            holidayA.year = 2022
            holidayA.month = 6
            holidayA.day = 5
            holidayA.name = "Helluntai"
            return listOf(holidayA)
        }
    }
}