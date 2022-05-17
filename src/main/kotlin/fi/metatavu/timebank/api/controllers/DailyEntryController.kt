package fi.metatavu.timebank.api.controllers

import fi.metatavu.timebank.api.persistence.repositories.DailyEntryRepository
import fi.metatavu.timebank.api.persistence.model.DailyEntry
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class DailyEntryController {

    @Inject
    lateinit var dailyEntryRepository: DailyEntryRepository

    suspend fun list(): MutableList<DailyEntry>? {
        return dailyEntryRepository.getAllEntries()
    }
}