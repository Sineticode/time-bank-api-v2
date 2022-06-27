package fi.metatavu.timebank.api.impl

import fi.metatavu.timebank.api.controllers.TimeEntryController
import fi.metatavu.timebank.spec.TimeEntriesApi
import java.util.UUID
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.ws.rs.core.Response

/**
 * API implementation for TimeEntries API
 */
@RequestScoped
class TimeEntriesApi: TimeEntriesApi, AbstractApi() {

    @Inject
    lateinit var timeEntryController: TimeEntryController

    override suspend fun deleteTimeEntry(entryId: UUID): Response {
        loggedUserId ?: return createUnauthorized(message = "Invalid token!")

        timeEntryController.deleteEntry(entryId = entryId)

        return createNoContent()
    }

}