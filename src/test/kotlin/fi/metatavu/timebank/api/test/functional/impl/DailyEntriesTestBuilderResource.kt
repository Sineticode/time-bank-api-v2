package fi.metatavu.timebank.api.test.functional.impl

import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenProvider
import fi.metatavu.timebank.api.test.functional.TestBuilder
import fi.metatavu.timebank.api.test.functional.settings.ApiTestSettings
import fi.metatavu.timebank.test.client.apis.DailyEntriesApi
import fi.metatavu.timebank.test.client.infrastructure.ApiClient
import fi.metatavu.timebank.test.client.models.TimeEntry

class DailyEntriesTestBuilderResource(
    testBuilder: TestBuilder,
    private val accessTokenProvider: AccessTokenProvider?,
    apiClient: ApiClient
): ApiTestBuilderResource<TimeEntry, ApiClient?>(testBuilder, apiClient) {

    override fun clean(timeEntry: fi.metatavu.timebank.test.client.models.TimeEntry) {

    }

    override fun getApi(): fi.metatavu.timebank.test.client.apis.DailyEntriesApi {
        ApiClient.accessToken = accessTokenProvider?.accessToken
        return DailyEntriesApi(ApiTestSettings.apiBasePath)
    }

}