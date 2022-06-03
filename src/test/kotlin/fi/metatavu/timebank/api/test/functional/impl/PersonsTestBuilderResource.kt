package fi.metatavu.timebank.api.test.functional.impl

import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenProvider
import fi.metatavu.timebank.api.test.functional.TestBuilder
import fi.metatavu.timebank.api.test.functional.settings.ApiTestSettings
import fi.metatavu.timebank.api.test.functional.tests.TestData
import fi.metatavu.timebank.test.client.apis.PersonsApi
import fi.metatavu.timebank.test.client.infrastructure.ApiClient
import fi.metatavu.timebank.model.Person

class PersonsTestBuilderResource(
    testBuilder: TestBuilder,
    private val accessTokenProvider: AccessTokenProvider?,
    apiClient: ApiClient
): ApiTestBuilderResource<Person, ApiClient?>(testBuilder, apiClient) {

    override fun clean(person: Person) {

    }

    override fun getApi(): PersonsApi {
        ApiClient.accessToken = accessTokenProvider?.accessToken
        return PersonsApi(ApiTestSettings.apiBasePath)
    }

    fun getPersons(): Array<Person> {
        return api.listPersons(false)
    }

}