package fi.metatavu.timebank.api.controllers

import fi.metatavu.timebank.api.persistence.repositories.TimeEntryRepository
import fi.metatavu.timebank.api.persistence.model.TimeEntry
import java.time.LocalDate
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

/**
 * Controller for DailyEntry objects
 */
@ApplicationScoped
class DailyEntryController {

    @Inject
    lateinit var timeEntryRepository: TimeEntryRepository

    suspend fun list(personId: Int?, before: LocalDate?, after: LocalDate?): List<TimeEntry>? {
        return timeEntryRepository.getAllEntries(personId, before, after)
    }
}