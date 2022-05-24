package fi.metatavu.timebank.api.impl

import fi.metatavu.timebank.spec.SynchronizeApi
import java.time.LocalDate
import javax.enterprise.context.RequestScoped
import javax.ws.rs.core.Response

/**
 * API implementation for Synchronize API
 */
@RequestScoped
class SynchronizeApi:  SynchronizeApi, AbstractApi() {

    override suspend fun synchronizeTimeEntries(before: LocalDate?, after: LocalDate?): Response {
        return createOk()
    }
}