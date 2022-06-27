package fi.metatavu.timebank.api.impl

import fi.metatavu.timebank.api.controllers.DailyEntryController
import fi.metatavu.timebank.spec.DailyEntriesApi
import java.time.LocalDate
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.ws.rs.core.Response

/**
 * API implementation for DailyEntries API
 */
@RequestScoped
class DailyEntriesApi: DailyEntriesApi, AbstractApi() {

    @Inject
    lateinit var dailyEntryController: DailyEntryController

    override suspend fun listDailyEntries(personId: Int?, before: LocalDate?, after: LocalDate?): Response {
        loggedUserId ?: return createUnauthorized("No logged in user!")
        return try {
            val entries = dailyEntryController.list(
                personId = personId,
                before = before,
                after = after
            ) ?: return createNotFound("No daily entries found!")

            createOk(entity = entries)
        } catch (e: Error) {
            createBadRequest(e.localizedMessage)
        }
    }
}