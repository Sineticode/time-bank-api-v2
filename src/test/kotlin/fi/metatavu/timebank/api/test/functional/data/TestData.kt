package fi.metatavu.timebank.api.test.functional.data

import fi.metatavu.timebank.api.forecast.models.ForecastPerson

/**
 * Class used by Wiremock to retrieve mock test data
 */
class TestData {

    companion object {
        /**
         * Gets one person
         *
         * @param id personId
         * @return ForecastPerson
         */
        fun getPerson(id: Int): ForecastPerson {
            return TestPersonsData.getPersons().filter { it.id == id }[0]
        }

        /**
         * Gets all persons
         *
         * @return List of ForecastPerson
         */
        fun getPersons(): List<ForecastPerson> {
            return TestPersonsData.getPersons()
        }
    }
}