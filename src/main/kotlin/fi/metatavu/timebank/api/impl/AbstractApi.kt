package fi.metatavu.timebank.api.impl

import javax.ws.rs.core.Response

abstract class AbstractApi {

    protected fun createOk(entity: Any?): Response {
        return Response
            .status(Response.Status.OK)
            .entity(entity)
            .build()
    }

    protected fun createOk(): Response {
        return Response
            .status(Response.Status.OK)
            .build()
    }

    protected fun createBadRequest(message: String): Response {
        return createError(Response.Status.BAD_REQUEST, message)
    }

    private fun createError(status: Response.Status, message: String): Response {
        val entity = fi.metatavu.timebank.model.Error(
            code = status.statusCode,
            message = message
        )
        return Response
            .status(status)
            .entity(entity)
            .build()
    }
}