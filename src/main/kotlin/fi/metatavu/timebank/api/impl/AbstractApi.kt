package fi.metatavu.timebank.api.impl

import io.quarkus.security.identity.SecurityIdentity
import org.eclipse.microprofile.jwt.JsonWebToken
import java.util.*
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.ws.rs.core.Response


/**
 * Abstract base class for all API services
 *
 * @author Jari Nykänen
 */
@RequestScoped
abstract class AbstractApi {

    @Inject
    lateinit var jsonWebToken: JsonWebToken

    @Inject
    lateinit var identity: SecurityIdentity

    /**
     * Returns logged user id
     *
     * @return logged user id
     */
    protected val loggedUserId: UUID?
        get() {
            if (jsonWebToken.subject != null) {
                return UUID.fromString(jsonWebToken.subject)
            }

            return null
        }

    /**
     * Checks if user is manager
     *
     * @return if user is manager
     */
//    protected fun isManager(): Boolean {
//        return identity.hasRole(UserRole.MANAGER.name)
//    }

    /**
     * Constructs ok response with total count header
     *
     * @param entity payload
     * @return response
     */
    protected fun createOk(entity: Any?, count: Long): Response {
        return Response
            .status(Response.Status.OK)
            .entity(entity)
            .header("X-Total-Count", count.toString())
            .header("Access-Control-Expose-Headers", "X-Total-Count")
            .build()
    }

    /**
     * Constructs ok response
     *
     * @param entity payload
     * @return response
     */
    protected fun createOk(entity: Any?): Response {
        return Response
            .status(Response.Status.OK)
            .entity(entity)
            .build()
    }

    /**
     * Constructs ok response
     *
     * @return response
     */
    protected fun createOk(): Response {
        return Response
            .status(Response.Status.OK)
            .build()
    }

    /**
     * Constructs no content response
     *
     * @param entity payload
     * @return response
     */
    protected fun createAccepted(entity: Any?): Response {
        return Response
            .status(Response.Status.ACCEPTED)
            .entity(entity)
            .build()
    }

    /**
     * Constructs no content response
     *
     * @return response
     */
    protected fun createNoContent(): Response {
        return Response
            .status(Response.Status.NO_CONTENT)
            .build()
    }

    /**
     * Constructs bad request response
     *
     * @param message message
     * @return response
     */
    protected fun createBadRequest(message: String): Response {
        return createError(Response.Status.BAD_REQUEST, message)
    }

    /**
     * Constructs not found response
     *
     * @param message message
     * @return response
     */
    protected fun createNotFound(message: String): Response {
        return createError(Response.Status.NOT_FOUND, message)
    }

    /**
     * Constructs not found response
     *
     * @return response
     */
    protected fun createNotFound(): Response {
        return Response
            .status(Response.Status.NOT_FOUND)
            .build()
    }

    /**
     * Constructs not found response
     *
     * @param message message
     * @return response
     */
    protected fun createConflict(message: String): Response {
        return createError(Response.Status.CONFLICT, message)
    }

    /**
     * Constructs not implemented response
     *
     * @param message message
     * @return response
     */
    protected fun createNotImplemented(message: String): Response {
        return createError(Response.Status.NOT_IMPLEMENTED, message)
    }

    /**
     * Constructs internal server error response
     *
     * @param message message
     * @return response
     */
    protected fun createInternalServerError(message: String): Response {
        return createError(Response.Status.INTERNAL_SERVER_ERROR, message)
    }

    /**
     * Constructs forbidden response
     *
     * @param message message
     * @return response
     */
    protected fun createForbidden(message: String): Response {
        return createError(Response.Status.FORBIDDEN, message)
    }

    /**
     * Constructs unauthorized response
     *
     * @param message message
     * @return response
     */
    protected fun createUnauthorized(message: String): Response {
        return createError(Response.Status.UNAUTHORIZED, message)
    }

    /**
     * Constructs an error response
     *
     * @param status status code
     * @param message message
     *
     * @return error response
     */
    private fun createError(status: Response.Status, message: String): Response {
        val entity = fi.metatavu.timebank.model.Error(
            message = message,
            code = status.statusCode
        )

        return Response
            .status(status)
            .entity(entity)
            .build()
    }

    /**
     * Creates error message of target not belonging to customer
     *
     * @param target target object name
     * @param targetId object id
     * @param customerId customer id
     * @return error message
     */
    fun createDoesNotBelongToCustomer(target: String, targetId: UUID?, customerId: UUID?): String {
        return "$target $targetId does not belong to customer $customerId"
    }

    /**
     * Creates error message for not having enough rights to access customer
     *
     * @param userId user id
     * @param customerId customer id
     * @return created message
     */
    fun createNoRightsForCustomerMessage(userId: UUID, customerId: UUID): String {
        return "$userId has not rights to access $customerId"
    }

    /**
     * Constructs the message for machine that is not assigned to any customer
     *
     * @param machineId machine id
     * @return message
     */
    fun getMachineNoCustomer(machineId: UUID): String {
        return "$machineId does not belong to any customer!"
    }

    /**
     * Constructs the message for group id missing error
     *
     * @param customerId customer id
     * @return message
     */
    fun getNoGroupId(customerId: UUID): String {
        return "Customer $customerId didn't have group id"
    }

    /**
     * Creates not found message with given parameters
     *
     * @param target target of the find method
     * @param id ID of the target
     */
    protected fun createNotFoundMessage(target: String, id: UUID): String {
        return "$target with ID $id could not be found"
    }

    companion object {
        const val NO_VALID_USER_MESSAGE = "No valid user!"
        const val CUSTOMER_NO_ID = "Customer has no id!"
        const val NOT_FOUND_MESSAGE = "Not found"
        const val UNAUTHORIZED = "Unauthorized"
        const val FORBIDDEN = "Forbidden"
        const val MISSING_REQUEST_BODY = "Missing request body"

        const val CUSTOMER = "Customer"
        const val ALARM = "Alarm"
        const val ALARM_DATA = "Alarm data"
        const val MACHINE = "Machine"
        const val ITEM = "Item"
        const val ITEM_EVENT = "Item event"
        const val MACHINE_EVENT = "Machine event"
        const val SLOT = "Slot"
        const val INVALID_OBJECT = "Invalid data object!"
    }

}
