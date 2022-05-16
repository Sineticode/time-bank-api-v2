package fi.metatavu.timebank.api.impl

import fi.metatavu.timebank.api.controllers.DailyEntryController
import fi.metatavu.timebank.api.persistence.model.DailyEntry
import fi.metatavu.timebank.spec.DailyEntriesApi
import java.time.LocalDate
import javax.inject.Inject
import javax.ws.rs.core.Response

class DailyEntriesApi: DailyEntriesApi, AbstractApi() {

    @Inject
    lateinit var dailyEntryController: DailyEntryController

    override fun listDailyEntries(personId: Int?, before: LocalDate?, after: LocalDate?): Response {
        val dailyEntries: List<DailyEntry> = dailyEntryController.list()
        return createOk(dailyEntries)
    }
}