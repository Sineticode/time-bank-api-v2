package fi.metatavu.timebank.api.test.functional.data

import fi.metatavu.timebank.api.forecast.models.ForecastPerson

/**
 * Class for test persons mock data
 */
class TestPersonsData {

    companion object {

        fun getPersons(): List<ForecastPerson> {
            return listOf(
                createTestPerson(
                    id = 1,
                    firstName = "TesterA",
                    lastName = "TestA",
                    monday = 435,
                    tuesday = 435,
                    wednesday = 435,
                    thursday = 435,
                    friday = 435,
                    saturday = 0,
                    sunday = 0,
                    active = true,
                    language = "ENGLISH_UK",
                    holidayCalendarId = 123456,
                    startDate = "2022-05-11",
                    createdAt = "2022-05-11",
                    isSystemUser = false
                ),
                createTestPerson(
                    id = 2,
                    firstName = "TesterB",
                    lastName = "TestB",
                    monday = 435,
                    tuesday = 435,
                    wednesday = 435,
                    thursday = 435,
                    friday = 435,
                    saturday = 0,
                    sunday = 0,
                    active = false,
                    language = "ENGLISH_UK",
                    holidayCalendarId = 123456,
                    startDate = "2022-05-05",
                    createdAt = "2022-05-05",
                    isSystemUser = false
                ),
                createTestPerson(
                    id = 3,
                    firstName = "TesterC",
                    lastName = "TestC",
                    monday = 435,
                    tuesday = 435,
                    wednesday = 435,
                    thursday = 435,
                    friday = 435,
                    saturday = 0,
                    sunday = 0,
                    active = false,
                    language = "FINNISH",
                    holidayCalendarId = 123456,
                    startDate = "2022-05-11",
                    createdAt = "2022-05-11",
                    isSystemUser = false
                ),
                createTestPerson(
                    id = 4,
                    firstName = "TEST_API_SYSTEM",
                    lastName = "",
                    monday = 0,
                    tuesday = 0,
                    wednesday = 0,
                    thursday = 0,
                    friday = 0,
                    saturday = 0,
                    sunday = 0,
                    active = true,
                    language = "ENGLISH_UK",
                    holidayCalendarId = 123456,
                    startDate = "2022-05-11",
                    createdAt = "2022-05-11",
                    isSystemUser = true
                ),
                createTestPerson(
                    id = 5,
                    firstName = "TesterE",
                    lastName = "Updater",
                    monday = 435,
                    tuesday = 435,
                    wednesday = 435,
                    thursday = 435,
                    friday = 435,
                    saturday = 0,
                    sunday = 0,
                    active = true,
                    language = "RUSSIAN",
                    holidayCalendarId = 123456,
                    startDate = "2022-06-30",
                    createdAt = "2022-06-30",
                    isSystemUser = false,
                )
            )

        }

        private fun createTestPerson(
            id: Int,
            firstName: String,
            lastName: String,
            monday: Int,
            tuesday: Int,
            wednesday: Int,
            thursday: Int,
            friday: Int,
            saturday: Int,
            sunday: Int,
            active: Boolean,
            language: String,
            holidayCalendarId: Int,
            startDate: String,
            createdAt: String,
            isSystemUser: Boolean
        ): ForecastPerson {
            val newPerson = ForecastPerson()
            newPerson.id = id
            newPerson.firstName = firstName
            newPerson.lastName = lastName
            newPerson.monday = monday
            newPerson.tuesday = tuesday
            newPerson.wednesday = wednesday
            newPerson.thursday = thursday
            newPerson.friday = friday
            newPerson.saturday = saturday
            newPerson.sunday = sunday
            newPerson.active = active
            newPerson.language = language
            newPerson.holidayCalendarId = holidayCalendarId
            newPerson.startDate = startDate
            newPerson.createdAt = createdAt
            newPerson.isSystemUser = isSystemUser

            return newPerson
        }
        
        fun getUpdatedPersons(): List<ForecastPerson> {
            val updatedPerson = ForecastPerson()
            updatedPerson.id = 5
            updatedPerson.firstName = "TesterE"
            updatedPerson.lastName = "Updater"
            updatedPerson.email = ""
            updatedPerson.monday = 217
            updatedPerson.tuesday = 217
            updatedPerson.wednesday = 217
            updatedPerson.thursday = 217
            updatedPerson.friday = 0
            updatedPerson.saturday = 0
            updatedPerson.sunday = 0
            updatedPerson.active = true
            updatedPerson.language = "RUSSIAN"
            updatedPerson.holidayCalendarId = 123456
            updatedPerson.startDate = "2022-06-30"
            updatedPerson.endDate = null
            updatedPerson.createdAt = "2022-06-30"
            updatedPerson.updatedAt = null
            updatedPerson.isSystemUser = false
            return listOf(updatedPerson)
        }
    }
}