package fi.metatavu.timebank.api.controllers

import fi.metatavu.timebank.api.persistence.model.TimeEntry
import fi.metatavu.timebank.api.persistence.repositories.TimeEntryRepository
import java.time.LocalDate
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
     * Gets persisted timeEntries
     *
     * @param personId personId
     * @param before before
     * @param after after
     * @param vacation vacation
     * @return List of TimeEntries
     */
    suspend fun getEntries(personId: Int?, before: LocalDate?, after: LocalDate?, vacation: Boolean?): List<TimeEntry> {
        return timeEntryRepository.getAllEntries(
            personId = personId,
            before = before,
            after = after,
            vacation = vacation
        )
    }

    /**
     * Deletes given persisted timeEntry
     *
     * @param entryId entryId
     */
    suspend fun deleteEntry(entryId: UUID) {
        timeEntryRepository.deleteEntry(entryId = entryId)
    }
}