package fi.metatavu.timebank.api.controllers

import fi.metatavu.timebank.api.persistence.repositories.TimeEntryRepository
import fi.metatavu.timebank.api.persistence.model.TimeEntry
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

/**
 * Controller for DailyEntry objects
 */
@ApplicationScoped
class DailyEntryController {

    @Inject
    lateinit var timeEntryRepository: TimeEntryRepository

    suspend fun list(): List<TimeEntry>? {
        return timeEntryRepository.getAllEntries()
    }
}