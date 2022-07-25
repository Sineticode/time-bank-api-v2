package fi.metatavu.timebank.api.test.functional.data

import fi.metatavu.timebank.api.forecast.models.ForecastTimeEntry
import java.time.LocalDate
import java.time.OffsetDateTime

/**
 * Class for test time entries mock data
 */
class TestTimeEntriesData {

    companion object {

        /**
         * Gets list of mock ForecastTimeEntries
         *
         * @return List of ForecastTimeEntries
         */
        fun getForecastTimeEntries(): List<ForecastTimeEntry> {
            return listOf(
                createTestTimeEntry(
                    id = 0,
                    person = 2,
                    nonProjectTime = 123,
                    timeRegistered = 435,
                    date = "2021-07-31",
                    createdAt = "2021-07-31T12:00:00Z",
                    updatedAt = "2021-07-31T12:00:00Z"
                ),
                createTestTimeEntry(
                    id = 1,
                    person = 1,
                    nonProjectTime = 255455,
                    timeRegistered = 100,
                    date = "2022-06-05",
                    createdAt = "2022-05-30T04:53:33Z",
                    updatedAt = "2022-05-30T04:53:33Z"
                ),
                createTestTimeEntry(
                    id = 2,
                    person = 1,
                    nonProjectTime = null,
                    timeRegistered = 100,
                    date = "2022-05-30",
                    createdAt = "2022-05-30T04:53:33Z",
                    updatedAt = "2022-05-30T04:53:33Z"
                ),
                createTestTimeEntry(
                    id = 3,
                    person = 2,
                    nonProjectTime = null,
                    timeRegistered = 400,
                    date = "2022-05-12",
                    createdAt = "2022-05-30T04:53:33Z",
                    updatedAt = "2022-05-30T04:53:33Z"
                ),
                createTestTimeEntry(
                    id = 4,
                    person = 2,
                    nonProjectTime = null,
                    timeRegistered = 400,
                    date = "2022-04-30",
                    createdAt = "2022-05-30T04:53:33Z",
                    updatedAt = "2022-05-30T04:53:33Z"
                ),
                createTestTimeEntry(
                    id = 5,
                    person = 3,
                    nonProjectTime = null,
                    timeRegistered = 122,
                    date = "2022-04-02",
                    createdAt = "2022-05-30T04:53:33Z",
                    updatedAt = "2022-05-30T04:53:33Z"
                ),
                createTestTimeEntry(
                    id = 6,
                    person = 3,
                    nonProjectTime = 255455,
                    timeRegistered = 372,
                    date = "2022-03-28",
                    createdAt = "2022-05-30T04:53:33Z",
                    updatedAt = "2022-05-30T04:53:33Z"
                ),
                createTestTimeEntry(
                    id = 7,
                    person = 3,
                    nonProjectTime = 114753,
                    timeRegistered = 378,
                    date = "2022-03-16",
                    createdAt = "2022-05-30T04:53:33Z",
                    updatedAt = "2022-05-30T04:53:33Z"
                ),
                createTestTimeEntry(
                    id = 8,
                    person = 3,
                    nonProjectTime = null,
                    timeRegistered = 122,
                    date = "2022-03-14",
                    createdAt = "2022-05-30T04:53:33Z",
                    updatedAt = "2022-05-30T04:53:33Z"
                ),
                createTestTimeEntry(
                    id = 9,
                    person = 3,
                    nonProjectTime = null,
                    timeRegistered = 52,
                    date = "2022-01-03",
                    createdAt = "2022-05-30T04:53:33Z",
                    updatedAt = "2022-05-30T04:53:33Z"
                ),
                createTestTimeEntry(
                    id = 10,
                    person = 1,
                    nonProjectTime = 12,
                    timeRegistered = 120,
                    date = "2022-02-28",
                    createdAt = "2022-02-28T04:53:33Z",
                    updatedAt = "2022-03-01T04:53:33Z"
                ),
                createTestTimeEntry(
                    id = 11,
                    person = 2,
                    nonProjectTime = null,
                    timeRegistered = 400,
                    date = "2022-06-07",
                    createdAt = "2022-06-07T04:53:33Z",
                    updatedAt = "2022-06-07T04:53:33Z"
                ),
                createTestTimeEntry(
                    id = 12,
                    person = 1,
                    nonProjectTime = null,
                    timeRegistered = 312,
                    date = "2022-05-29",
                    createdAt = "2022-05-29T04:53:33Z",
                    updatedAt = "2022-05-29T04:53:33Z"
                ),
                createTestTimeEntry(
                    id = 13,
                    person = 2,
                    nonProjectTime = 1,
                    timeRegistered = 213,
                    date = "2022-06-10",
                    createdAt = "2022-06-10T04:53:33Z",
                    updatedAt = "2022-06-10T04:53:33Z"
                ),
                createTestTimeEntry(
                    id = 14,
                    person = 5,
                    nonProjectTime = null,
                    timeRegistered = 435,
                    date = "2022-06-30",
                    createdAt = "2022-06-30T12:00:00Z",
                    updatedAt = "2022-06-30T12:00:00Z"
                ),
                createTestTimeEntry(
                    id = 15,
                    person = 1,
                    nonProjectTime = 228255,
                    timeRegistered = 435,
                    date = "2022-07-04",
                    createdAt = "2022-07-04T08:00:00Z",
                    updatedAt = "2022-07-04T08:00:00Z"
                ),
                createTestTimeEntry(
                    id = 16,
                    person = 1,
                    nonProjectTime = null,
                    timeRegistered = 42,
                    date = "2022-07-04",
                    createdAt = "2022-07-04T11:00:00Z",
                    updatedAt = "2022-07-04T11:00:00Z"
                )
            )
        }

        /**
         * Gets list of mock ForecastTimeEntries.
         * Mocks that an existing entry has been updated.
         *
         * @return List of ForecastTimeEntries
         */
        fun getUpdatedForecastTimeEntry(): List<ForecastTimeEntry> {
            return listOf(
                createTestTimeEntry(
                    id = 14,
                    person = 5,
                    nonProjectTime = 789,
                    timeRegistered = 435,
                    date = "2022-06-30",
                    createdAt = "2022-06-30T12:00:00Z",
                    updatedAt = "2022-06-30T16:00:00Z"
                )
            )
        }

        /**
         * Gets list of mock ForecastTimeEntries.
         * Contains entry for person whose expected worktime has changed.
         *
         * @return List of ForecastTimeEntries
         */
        fun getForecastTimeEntryForUpdatedPerson(): List<ForecastTimeEntry> {
            return listOf(
                createTestTimeEntry(
                    id = 15,
                    person = 5,
                    nonProjectTime = 789,
                    timeRegistered = 435,
                    date = LocalDate.now().toString(),
                    createdAt = getTodayODT(),
                    updatedAt = getTodayODT()
                )
            )
        }

        /**
         * Gets list of mock ForecastTimeEntries.
         * Mocks paginated API response where is more than one page of entries.
         *
         * @return List of ForecastTimeEntries
         */
        fun generateRandomForecastTimeEntries(pageNumber: Int): List<ForecastTimeEntry> {
            val generatedEntries = mutableListOf<ForecastTimeEntry>()
            val amountToGenerate = if (pageNumber == 1) 1000 else 200
            var id = pageNumber * 1000
            while (generatedEntries.size < amountToGenerate) {
                val year = (2021..2022).random()
                val month = (1..12).random()
                val day =
                    if (month == 2) (1..28).random()
                    else if (month % 2 != 0 && month < 8) (1..31).random()
                    else if (month % 2 == 0 && month >= 8
                    ) (1..31).random()
                    else (1..30).random()
                val monthString = if (month < 10) "0$month" else "$month"
                val dayString = if (day < 10) "0$day" else "$day"
                val date = "$year-$monthString-$dayString"
                val person = (100000..900000).random()
                if (generatedEntries.find { it.id == id } == null) {
                    generatedEntries.add(createTestTimeEntry(
                        id = id,
                        person = person,
                        nonProjectTime = if ((0..1).random() != 0) 1 else null,
                        timeRegistered = (1..435).random(),
                        date = date,
                        createdAt = "${date}T12:00:00Z",
                        updatedAt = "${date}T12:00:00Z"
                    ))
                } else {
                    continue
                }
                id++
            }

            return generatedEntries
        }

        /**
         * Helper method for simplifying creating of ForecastTimeEntry objects
         *
         * @param id id
         * @param person person
         * @param nonProjectTime nonProjectTime
         * @param timeRegistered timeRegistered
         * @param date date
         * @param createdAt createdAt
         * @param updatedAt
         * @return ForecastTimeEntry
         */
        private fun createTestTimeEntry(
            id: Int,
            person: Int,
            nonProjectTime: Int?,
            timeRegistered: Int,
            date: String,
            createdAt: String,
            updatedAt: String
        ): ForecastTimeEntry {
            val newTimeEntry = ForecastTimeEntry()
            newTimeEntry.id = id
            newTimeEntry.person = person
            newTimeEntry.nonProjectTime = nonProjectTime
            newTimeEntry.timeRegistered = timeRegistered
            newTimeEntry.date = date
            newTimeEntry.createdBy = person
            newTimeEntry.updatedBy = person
            newTimeEntry.createdAt = createdAt
            newTimeEntry.updatedAt = updatedAt

            return newTimeEntry
        }

        /**
         * Gets today date as OffsetDateTime and removes time offset
         *
         * @return String OffsetDateTime without time offset
         */
        private fun getTodayODT(): String {
            val dateString = OffsetDateTime.now().toString().split("+")
            return "${dateString[0]}Z"
        }
    }
}