package fi.metatavu.timebank.api.controllers

import fi.metatavu.timebank.api.persistence.repositories.DailyEntryRepository
import fi.metatavu.timebank.api.persistence.model.DailyEntry
import java.time.LocalDate
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class DailyEntryController {

    @Inject
    lateinit var dailyEntryRepository: DailyEntryRepository

    suspend fun list(personId: Int?, before: LocalDate?, after: LocalDate?): MutableList<DailyEntry> {
        return dailyEntryRepository.getAllEntries(personId, before, after)
    }

    suspend fun list(personId: Int): MutableList<DailyEntry> {
        return dailyEntryRepository.getEntriesById(personId)
    }
}