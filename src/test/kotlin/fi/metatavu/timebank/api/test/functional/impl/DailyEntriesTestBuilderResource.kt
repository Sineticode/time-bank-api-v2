package fi.metatavu.timebank.api.test.functional.impl

import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenProvider
import fi.metatavu.timebank.api.test.functional.TestBuilder
import fi.metatavu.timebank.api.test.functional.settings.ApiTestSettings
import fi.metatavu.timebank.test.client.apis.DailyEntriesApi
import fi.metatavu.timebank.test.client.infrastructure.ApiClient
import fi.metatavu.timebank.test.client.infrastructure.ClientException
import fi.metatavu.timebank.test.client.models.DailyEntry
import org.junit.Assert

/**
 * Test builder resource for Daily Entries API
 */
class DailyEntriesTestBuilderResource(
    testBuilder: TestBuilder,
    private val accessTokenProvider: AccessTokenProvider?,
    apiClient: ApiClient
): ApiTestBuilderResource<DailyEntry, ApiClient?>(testBuilder, apiClient) {

    override fun clean(t: DailyEntry?) {
        TODO("Not yet implemented")
    }


    override fun getApi(): DailyEntriesApi {
        ApiClient.accessToken = accessTokenProvider?.accessToken
        return DailyEntriesApi(ApiTestSettings.apiBasePath)
    }

    /**
     * Gets daily entries of given person
     *
     * @param personId person id
     * @param before before date
     * @param after after date
     * @return list of daily entries
     */
    fun getDailyEntries(personId: Int?, before: String?, after: String?): Array<DailyEntry> {
        return api.listDailyEntries(
            personId = personId,
            before = before,
            after = after
        )
    }

    /**
     * Asserts that listing daily entries fails with given status
     *
     * @param expectedStatus expected status
     * @param id person id
     * @param before before date
     * @param after after date
     */
    fun assertListFail(expectedStatus: Int, id: Int?, before: String?, after: String?) {
        try{
            api.listDailyEntries(
                personId = id,
                before = before,
                after = after
            )
            Assert.fail(String.format("Expected fail with status, $expectedStatus"))
        } catch (ex: ClientException) {
            assertClientExceptionStatus(expectedStatus, ex)
        }
    }
}