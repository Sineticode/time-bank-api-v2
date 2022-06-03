package fi.metatavu.timebank.api.test.functional.impl

import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenProvider
import fi.metatavu.timebank.api.test.functional.TestBuilder
import fi.metatavu.timebank.api.test.functional.settings.ApiTestSettings
import fi.metatavu.timebank.test.client.apis.SynchronizeApi
import fi.metatavu.timebank.test.client.infrastructure.ApiClient
import fi.metatavu.timebank.test.client.models.TimeEntry
import java.util.*

class SynchronizeTestBuilderResource(
    testBuilder: TestBuilder,
    private val accessTokenProvider: AccessTokenProvider?,
    apiClient: ApiClient
): ApiTestBuilderResource<TimeEntry, ApiClient?>(testBuilder, apiClient) {

    override fun clean(timeEntry: TimeEntry) {

    }

    override fun getApi(): SynchronizeApi {
        ApiClient.accessToken = accessTokenProvider?.accessToken
        return SynchronizeApi(ApiTestSettings.apiBasePath)
    }

    fun synchronizeEntries() {
        api.synchronizeTimeEntries(
            before = null,
            after = "2022-05-10"
        )
    }

}