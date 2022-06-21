package fi.metatavu.timebank.api.test.functional.data

import fi.metatavu.timebank.api.forecast.models.ForecastPerson

/**
 * Class for test persons mock data
 */
class TestPersonsData {

    companion object {

        /**
         * Gets list mock ForecastPersons
         *
         * @return List of ForecastPersons
         */
        fun getPersons(): List<ForecastPerson> {
            return listOf(
                ForecastPerson(
                    id = 1,
                    first_name = "TesterA",
                    last_name = "TestA",
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
                ),
                ForecastPerson(
                    id = 2,
                    first_name = "TesterB",
                    last_name = "TestB",
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
                ),
                ForecastPerson(
                    id = 3,
                    first_name = "TesterC",
                    last_name = "TestC",
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
                ),
                ForecastPerson(
                    id = 4,
                    first_name = "TEST_API_SYSTEM",
                    last_name = null,
                    email = null,
                    monday = 0,
                    tuesday = 0,
                    wednesday = 0,
                    thursday = 0,
                    friday = 0,
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
                    is_system_user = true
                )
            )
        }
    }
}