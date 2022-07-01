package fi.metatavu.timebank.api.test.functional.data

import fi.metatavu.timebank.api.forecast.models.ForecastHoliday
import fi.metatavu.timebank.api.forecast.models.ForecastPerson
import fi.metatavu.timebank.api.forecast.models.ForecastTimeEntry
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

                createForecastTimeEntryResponse(
                    pageContents = pageContents,
                    pageSize = pageContents.size,
                    totalObjectCount = pageContents.size
                )
            } else if (before != null) {
                val pageContents = TestTimeEntriesData.getForecastTimeEntries().filter { forecastTimeEntry ->
                    LocalDate.parse(forecastTimeEntry.date) < LocalDate.parse(before)
                }

                createForecastTimeEntryResponse(
                    pageContents = pageContents,
                    pageSize = pageContents.size,
                    totalObjectCount = pageContents.size
                )
            } else {
                createForecastTimeEntryResponse(
                    pageContents = TestTimeEntriesData.getForecastTimeEntries(),
                    pageSize = TestTimeEntriesData.getForecastTimeEntries().size,
                    totalObjectCount = TestTimeEntriesData.getForecastTimeEntries().size
                )
            }
        }

        /**
         * Gets mock Forecast API response to /v4/time_registrations
         * with time entries with updated values
         *
         * @return ForecastTimeEntryResponse
         */
        fun getUpdatedForecastTimeEntryResponse(): ForecastTimeEntryResponse {
            return createForecastTimeEntryResponse(
                pageContents = TestTimeEntriesData.getUpdatedForecastTimeEntry(),
                pageSize = 1,
                totalObjectCount = 1
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
            return createForecastTimeEntryResponse(
                pageContents = TestTimeEntriesData.generateRandomForecastTimeEntries(pageNumber),
                pageSize = 1000,
                totalObjectCount = 1200
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
         * Gets person whose worktime has been edited
         *
         * @return List of ForecastPerson
         */
        fun getUpdatedPersons(): List<ForecastPerson> {
            return TestPersonsData.getUpdatedPersons()
        }

        /**
         * Gets time registrations for person whose worktime has been edited
         *
         * @return ForecastTimeEntryResponse
         */
        fun getForecastTimeEntryResponseForUpdatedPerson(): ForecastTimeEntryResponse {
            return createForecastTimeEntryResponse(
                pageContents = TestTimeEntriesData.getForecastTimeEntryForUpdatedPerson(),
                pageSize = 1,
                totalObjectCount = 1
            )
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

        private fun createForecastTimeEntryResponse(
            pageContents: List<ForecastTimeEntry>,
            pageSize: Int,
            totalObjectCount: Int
        ): ForecastTimeEntryResponse {
            val newForecastResponse = ForecastTimeEntryResponse()
            newForecastResponse.pageContents = pageContents
            newForecastResponse.pageSize = pageSize
            newForecastResponse.totalObjectCount = totalObjectCount

            return newForecastResponse
        }
    }
}