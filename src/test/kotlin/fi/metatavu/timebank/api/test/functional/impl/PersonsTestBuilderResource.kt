package fi.metatavu.timebank.api.test.functional.impl

import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenProvider
import fi.metatavu.timebank.api.test.functional.TestBuilder
import fi.metatavu.timebank.api.test.functional.settings.ApiTestSettings
import fi.metatavu.timebank.test.client.apis.PersonsApi
import fi.metatavu.timebank.test.client.infrastructure.ApiClient
import fi.metatavu.timebank.test.client.infrastructure.ClientException
import fi.metatavu.timebank.test.client.models.Person
import fi.metatavu.timebank.test.client.models.PersonTotalTime
import fi.metatavu.timebank.test.client.models.Timespan
import org.junit.Assert

class PersonsTestBuilderResource(
    testBuilder: TestBuilder,
    private val accessTokenProvider: AccessTokenProvider?,
    apiClient: ApiClient
): ApiTestBuilderResource<Person, ApiClient?>(testBuilder, apiClient) {

    override fun clean(t: Person?) {
        TODO("Not yet implemented")
    }

    override fun getApi(): PersonsApi {
        ApiClient.accessToken = accessTokenProvider?.accessToken
        return PersonsApi(ApiTestSettings.apiBasePath)
    }

    fun getPersons(active: Boolean? = true): Array<Person> {
        return api.listPersons(
            active = active
        )
    }

    fun getPersonTotal(personId: Int, timespan: Timespan? = Timespan.aLLTIME): Array<PersonTotalTime> {
        return api.listPersonTotalTime(
            personId = personId,
            timespan = timespan
        )
    }

    fun assertListFail(expectedStatus: Int) {
        try{
            api.listPersons(
                active = null
            )
            Assert.fail(String.format("Expected fail with status, $expectedStatus"))
        } catch (ex: ClientException) {
            assertClientExceptionStatus(expectedStatus, ex)
        }
    }

}