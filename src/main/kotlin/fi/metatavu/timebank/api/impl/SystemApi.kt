package fi.metatavu.timebank.api.impl

import fi.metatavu.timebank.spec.SystemApi
import javax.ws.rs.core.Response

class SystemApi: SystemApi {
    override suspend fun ping(): Response {
        return Response.ok("Pong").build()
    }
}