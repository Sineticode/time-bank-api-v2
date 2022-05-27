package fi.metatavu.timebank.api.impl

import fi.metatavu.timebank.api.controllers.DailyEntryController
import fi.metatavu.timebank.model.Timespan
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
//        return createOk(dailyEntryController.list(personId, before, after))
        return createOk(dailyEntryController.getDailyTotal(personId, Timespan.ALL_TIME))
    }
}