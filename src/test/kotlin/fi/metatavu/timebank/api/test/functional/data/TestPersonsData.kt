package fi.metatavu.timebank.api.test.functional.data

import fi.metatavu.timebank.api.forecast.models.ForecastPerson

/**
 * Class for test persons mock data
 */
class TestPersonsData {

    companion object {

        fun getPersons(): List<ForecastPerson> {
            val personA = ForecastPerson()
            personA.id = 1
            personA.firstName = "TesterA"
            personA.lastName = "TestA"
            personA.email = ""
            personA.monday = 435
            personA.tuesday = 435
            personA.wednesday = 435
            personA.thursday = 435
            personA.friday = 435
            personA.saturday = 0
            personA.sunday = 0
            personA.active = true
            personA.language = "ENGLISH_UK"
            personA.holidayCalendarId = 123456
            personA.startDate = "2022-05-11"
            personA.endDate = null
            personA.createdAt = "2022-05-11"
            personA.updatedAt = null
            personA.isSystemUser = false
            val personB = ForecastPerson()
            personB.id = 2
            personB.firstName = "TesterB"
            personB.lastName = "TestB"
            personB.email = ""
            personB.monday = 435
            personB.tuesday = 435
            personB.wednesday = 435
            personB.thursday = 435
            personB.friday = 435
            personB.saturday = 0
            personB.sunday = 0
            personB.active = false
            personB.language = "ENGLISH_UK"
            personB.holidayCalendarId = 123456
            personB.startDate = "2022-05-05"
            personB.endDate = null
            personB.createdAt = "2022-05-05"
            personB.updatedAt = null
            personB.isSystemUser = false
            val personC = ForecastPerson()
            personC.id = 3
            personC.firstName = "TesterC"
            personC.lastName = "TestC"
            personC.email = ""
            personC.monday = 435
            personC.tuesday = 435
            personC.wednesday = 435
            personC.thursday = 435
            personC.friday = 435
            personC.saturday = 0
            personC.sunday = 0
            personC.active = false
            personC.language = "FINNISH"
            personC.holidayCalendarId = 123456
            personC.startDate = "2022-05-11"
            personC.endDate = null
            personC.createdAt = "2022-05-11"
            personC.updatedAt = null
            personC.isSystemUser = false
            val personD = ForecastPerson()
            personD.id = 4
            personD.firstName = "TEST_API_SYSTEM"
            personD.lastName = ""
            personD.email = ""
            personD.monday = 0
            personD.tuesday = 0
            personD.wednesday = 0
            personD.thursday = 0
            personD.friday = 0
            personD.saturday = 0
            personD.sunday = 0
            personD.active = true
            personD.language = "ENGLISH_UK"
            personD.holidayCalendarId = 123456
            personD.startDate = "2022-05-11"
            personD.endDate = null
            personD.createdAt = "2022-05-11"
            personD.updatedAt = null
            personD.isSystemUser = true
            val personE = ForecastPerson()
            personE.id = 5
            personE.firstName = "TesterE"
            personE.lastName = "Updater"
            personE.email = ""
            personE.monday = 435
            personE.tuesday = 435
            personE.wednesday = 435
            personE.thursday = 435
            personE.friday = 435
            personE.saturday = 0
            personE.sunday = 0
            personE.active = true
            personE.language = "RUSSIAN"
            personE.holidayCalendarId = 123456
            personE.startDate = "2022-06-30"
            personE.endDate = null
            personE.createdAt = "2022-06-30"
            personE.updatedAt = null
            personE.isSystemUser = false
            return listOf(personA, personB, personC, personD, personE)
        }
    }
}