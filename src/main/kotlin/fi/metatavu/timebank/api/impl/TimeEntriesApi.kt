package fi.metatavu.timebank.api.impl

import fi.metatavu.timebank.api.controllers.TimeEntryController
import javax.enterprise.context.RequestScoped
import fi.metatavu.timebank.spec.TimeEntriesApi
import java.util.*
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
        if (!isAdmin()) return createUnauthorized(message = "Only admin is allowed to delete timeEntries!")

        timeEntryController.deleteEntry(entryId = entryId)

        return createNoContent()
    }
}