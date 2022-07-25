package fi.metatavu.timebank.api.impl

import fi.metatavu.timebank.api.controllers.SynchronizeController
import fi.metatavu.timebank.spec.SynchronizeApi
import java.time.LocalDate
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.ws.rs.core.Response

/**
 * API implementation for Synchronize API
 */
@RequestScoped
class SynchronizeApi: SynchronizeApi, AbstractApi() {

    @Inject
    lateinit var synchronizeController: SynchronizeController

    override suspend fun synchronizeTimeEntries(before: LocalDate?, after: LocalDate?): Response {
        loggedUserId ?: return createUnauthorized(message = "Invalid token!")

        try {
            val synchronizedEntries = synchronizeController.synchronize(after)

            if (synchronizedEntries.isEmpty()) {
                return createNoContent()
            }

            return createNoContent()
        } catch (e: Error) {
            return createBadRequest(message = e.localizedMessage)
        }
    }
}