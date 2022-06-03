package fi.metatavu.timebank.api.test.functional.auth

import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenProvider
import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenTestBuilderAuthentication
import fi.metatavu.timebank.api.test.functional.TestBuilder
import fi.metatavu.timebank.api.test.functional.impl.DailyEntriesTestBuilderResource
import fi.metatavu.timebank.api.test.functional.impl.PersonsTestBuilderResource
import fi.metatavu.timebank.api.test.functional.settings.ApiTestSettings
import fi.metatavu.timebank.test.client.infrastructure.ApiClient

class TestBuilderAuthentication(
    private val testBuilder: TestBuilder,
    accessTokenProvider: AccessTokenProvider
): AccessTokenTestBuilderAuthentication<ApiClient>(testBuilder, accessTokenProvider) {

    private var accessTokenProvider: AccessTokenProvider? = accessTokenProvider

    var dailyEntries: DailyEntriesTestBuilderResource = DailyEntriesTestBuilderResource(testBuilder, this.accessTokenProvider, createClient())
    var persons: PersonsTestBuilderResource = PersonsTestBuilderResource(testBuilder, this.accessTokenProvider, createClient())

    override fun createClient(authProvider: AccessTokenProvider): ApiClient {
        val result = ApiClient(ApiTestSettings.apiBasePath)
        ApiClient.accessToken = authProvider.accessToken
        return result
    }
}