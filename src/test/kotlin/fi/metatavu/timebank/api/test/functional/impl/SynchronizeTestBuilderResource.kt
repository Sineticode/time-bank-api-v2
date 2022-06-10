package fi.metatavu.timebank.api.test.functional.impl

import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenProvider
import fi.metatavu.timebank.api.test.functional.TestBuilder
import fi.metatavu.timebank.api.test.functional.settings.ApiTestSettings
import fi.metatavu.timebank.test.client.apis.SynchronizeApi
import fi.metatavu.timebank.test.client.infrastructure.ApiClient
import fi.metatavu.timebank.test.client.infrastructure.ClientException
import fi.metatavu.timebank.test.client.models.SyncResponse
import fi.metatavu.timebank.test.client.models.TimeEntry
import org.junit.Assert

/**
 * Test builder resource for Synchronize API
 */
class SynchronizeTestBuilderResource(
    testBuilder: TestBuilder,
    private val accessTokenProvider: AccessTokenProvider?,
    apiClient: ApiClient
): ApiTestBuilderResource<TimeEntry, ApiClient?>(testBuilder, apiClient) {

    override fun clean(t: TimeEntry?) {
        TODO("Not yet implemented")
    }


    override fun getApi(): SynchronizeApi {
        ApiClient.accessToken = accessTokenProvider?.accessToken
        return SynchronizeApi(ApiTestSettings.apiBasePath)
    }

    /**
     * Synchronizes time entries
     *
     * @param before before date
     * @param after after date
     */
    fun synchronizeEntries(before: String? = null, after: String? = null): SyncResponse {
        return api.synchronizeTimeEntries(
            before = before,
            after = after
        )
    }

    fun assertSynchronizeFail(expectedStatus: Int) {
        try {
            api.synchronizeTimeEntries(
                before = null,
                after = null
            )
            Assert.fail(String.format("Expected fail with status, $expectedStatus"))
        } catch (ex: ClientException) {
            assertClientExceptionStatus(expectedStatus, ex)
        }
    }
}