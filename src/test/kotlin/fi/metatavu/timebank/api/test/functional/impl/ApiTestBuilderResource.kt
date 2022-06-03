package fi.metatavu.timebank.api.test.functional.impl

import fi.metatavu.jaxrs.test.functional.builder.AbstractAccessTokenApiTestBuilderResource
import fi.metatavu.timebank.api.test.functional.TestBuilder
import fi.metatavu.timebank.test.client.infrastructure.ApiClient
import fi.metatavu.timebank.test.client.infrastructure.ClientException
import org.junit.Assert

abstract class ApiTestBuilderResource<T, A>(
    testBuilder: TestBuilder,
    private val apiClient: ApiClient
): AbstractAccessTokenApiTestBuilderResource<T, A, ApiClient>(testBuilder) {

    /**
     * Returns API client
     *
     * @return API client
     */
    override fun getApiClient(): ApiClient {
        return apiClient
    }

    /**
     * Asserts that client exception has expected status code
     *
     * @param expectedStatus expected status code
     * @param e client exception
     */
    protected fun assertClientExceptionStatus(expectedStatus: Int, e: ClientException) {
        Assert.assertEquals(expectedStatus, e.statusCode)
    }

    /**
     * Asserts that server exception has expected status code
     *
     * @param expectedStatus expected status code
     * @param e server exception
     */
    protected fun assertServerExceptionStatus(expectedStatus: Int, e: ClientException) {
        Assert.assertEquals(expectedStatus, e.statusCode)
    }
}