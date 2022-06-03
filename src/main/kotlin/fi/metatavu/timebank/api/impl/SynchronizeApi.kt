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
class SynchronizeApi:  SynchronizeApi, AbstractApi() {

    @Inject
    lateinit var synchronizeController: SynchronizeController

    override suspend fun synchronizeTimeEntries(before: LocalDate?, after: LocalDate?): Response {
        loggedUserId ?: return createUnauthorized(message = "Invalid token!")

        val synchronizedEntries = synchronizeController.synchronize(after)
            ?: return createBadRequest(message = "Something went wrong with attempt to synchronize!")

        if (synchronizedEntries == 0) {
            return createNotFound(message = "Nothing to synchronize!")
        }

        return createOk(entity = "Synchronized $synchronizedEntries entries from Forecast!")
    }
}