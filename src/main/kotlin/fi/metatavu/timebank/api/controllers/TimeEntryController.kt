package fi.metatavu.timebank.api.controllers

import fi.metatavu.timebank.api.persistence.repositories.TimeEntryRepository
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

/**
 * Controller for timeEntry objects
 */
@ApplicationScoped
class TimeEntryController {

    @Inject
    lateinit var timeEntryRepository: TimeEntryRepository

    /**
     * Deletes given persisted timeEntry
     *
     * @param entryId entryId
     */
    suspend fun deleteEntry(entryId: UUID) {
        timeEntryRepository.deleteEntry(entryId = entryId)
    }
}