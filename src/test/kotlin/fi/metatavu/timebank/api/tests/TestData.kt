package fi.metatavu.timebank.api.tests

import fi.metatavu.timebank.model.Person

/**
 * Class for test data that is used by wiremock and tests
 */
class TestData {
    companion object{
        const val personData: String =
            """
            [ 
                {
                    "personId": 1234,
                    "firstName": "Test",
                    "lastName": "Tester",
                    "email": "test.tester@metatavu.fi",
                    "userType": null,
                    "clientId": null,
                    "holidayCalendarId": 19401,
                    "monday": 435,
                    "tuesday": 435,
                    "wednesday": 435,
                    "thursday": 435,
                    "friday": 435,
                    "saturday": 0,
                    "sunday": 0,
                    "active": true,
                    "defaultRole": 255859,
                    "cost": 44.0,
                    "language": "ENGLISH_UK",
                    "startDate": "2021-07-31",
                    "endDate": null,
                    "createdBy": 395941,
                    "updatedBy": 395952,
                    "createdAt": "2021-06-11T15:04:44Z",
                    "updatedAt": "2022-05-24T05:56:46Z"
                }
            ]   
            """

        const val personId: Int = 123

        fun getPersonA(): Person {
            return Person(
                id = 12345,
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
    }
}