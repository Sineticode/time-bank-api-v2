package fi.metatavu.timebank.api.test.functional.impl

import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenProvider
import fi.metatavu.timebank.api.test.functional.TestBuilder
import fi.metatavu.timebank.api.test.functional.settings.ApiTestSettings
import fi.metatavu.timebank.test.client.apis.TimeEntriesApi
import fi.metatavu.timebank.test.client.infrastructure.ApiClient
import fi.metatavu.timebank.test.client.models.TimeEntry

/**
 * Test builder resource for TimeEntries API
 */
class TimeEntriesTestBuilderResource(
    testBuilder: TestBuilder,
    private val accessTokenProvider: AccessTokenProvider?,
    apiClient: ApiClient
): ApiTestBuilderResource<TimeEntry, ApiClient?>(testBuilder, apiClient) {

    override fun clean(t: TimeEntry?) {
        api.deleteTimeEntry(t!!.entryId)
    }

    override fun getApi(): TimeEntriesApi {
        ApiClient.accessToken = accessTokenProvider?.accessToken
        return TimeEntriesApi(ApiTestSettings.apiBasePath)
    }
}