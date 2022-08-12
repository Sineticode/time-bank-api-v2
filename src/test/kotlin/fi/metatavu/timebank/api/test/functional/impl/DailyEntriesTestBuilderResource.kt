package fi.metatavu.timebank.api.test.functional.impl

import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenProvider
import fi.metatavu.timebank.api.test.functional.TestBuilder
import fi.metatavu.timebank.api.test.functional.settings.ApiTestSettings
import fi.metatavu.timebank.test.client.apis.DailyEntriesApi
import fi.metatavu.timebank.test.client.infrastructure.ApiClient
import fi.metatavu.timebank.test.client.infrastructure.ClientException
import fi.metatavu.timebank.test.client.infrastructure.ServerException
import fi.metatavu.timebank.test.client.models.DailyEntry
import java.util.concurrent.Callable
import org.junit.Assert

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

    /**
     * Asserts that listing DailyEntries fails with given status
     *
     * @param expectedStatus expected status code
     */
    fun assertListFail(expectedStatus: Int) {
        try {
            api.listDailyEntries(
                personId = 100000,
                before = null,
                after = null,
                vacation = false
            )
            Assert.fail(String.format("Expected fail with status, $expectedStatus"))
        } catch (ex: java.lang.RuntimeException) {
            when (ex) {
                is ClientException -> assertClientExceptionStatus(expectedStatus, ex)
                is ServerException -> assertServerExceptionStatus(expectedStatus, ex)
            }
        }
    }

    /**
     * Checks if DailyEntries contains entry matching given details
     *
     * @param personId personId
     * @param before before
     * @param after after
     * @param vacation vacation
     * @param expected expected worktime
     * @return Boolean whether criteria matched
     */
    fun checkDailyEntries(personId: Int?, before: String?, after: String?, vacation: Boolean?, expected: Int): Callable<Boolean> {
        return Callable {
            getDailyEntries(
                personId = personId,
                before = before,
                after = after,
                vacation = vacation
            )[0].expected == expected
        }
    }
}