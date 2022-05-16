package fi.metatavu.timebank.api.impl

import fi.metatavu.timebank.spec.SynchronizeApi
import java.time.LocalDate
import javax.enterprise.context.RequestScoped
import javax.ws.rs.core.Response

@RequestScoped
class SynchronizeApi:  SynchronizeApi {


    override suspend fun synchronizeTimeEntries(before: LocalDate?, after: LocalDate?): Response {
        return Response.ok().build()
    }
}
