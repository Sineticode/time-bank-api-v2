package fi.metatavu.timebank.api.test.functional.impl

import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenProvider
import fi.metatavu.timebank.api.test.functional.TestBuilder
import fi.metatavu.timebank.api.test.functional.settings.ApiTestSettings
import fi.metatavu.timebank.test.client.apis.DailyEntriesApi
import fi.metatavu.timebank.test.client.infrastructure.ApiClient
import fi.metatavu.timebank.test.client.models.DailyEntry

/**
 * Test builder resource for DailyEntries API
 */
class DailyEntriesTestBuilderResource(
    testBuilder: TestBuilder,
    private val accessTokenProvider: AccessTokenProvider?,
    apiClient: ApiClient
): ApiTestBuilderResource<DailyEntry, ApiClient?>(testBuilder, apiClient) {

    // This test builder resource doesn't clean anything therefore this is empty.
    override fun clean(t: DailyEntry?) {
    }

    override fun getApi(): DailyEntriesApi {
        ApiClient.accessToken = accessTokenProvider?.accessToken
        return DailyEntriesApi(ApiTestSettings.apiBasePath)

    }

    /**
     * Gets daily entries
     *
     * @param personId optional personId
     * @param before optional before date
     * @param after optional after date
     * @param vacation optional vacation filter
     * @return list of DailyEntries
     */
    fun getDailyEntries(personId: Int? = null, before: String? = null, after: String? = null, vacation: Boolean? = null): Array<DailyEntry> {
        return api.listDailyEntries(
            personId = personId,
            before = before,
            after = after,
            vacation = vacation
        )
    }
}