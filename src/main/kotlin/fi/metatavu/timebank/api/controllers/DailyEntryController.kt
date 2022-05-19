package fi.metatavu.timebank.api.controllers

import fi.metatavu.timebank.api.persistence.repositories.DailyEntryRepository
import fi.metatavu.timebank.api.persistence.model.DailyEntry
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

/**
 * Controller for DailyEntry objects
 */
@ApplicationScoped
class DailyEntryController {

    @Inject
    lateinit var dailyEntryRepository: DailyEntryRepository

    suspend fun list(): List<DailyEntry>? {
        return dailyEntryRepository.getAllEntries()
    }
}