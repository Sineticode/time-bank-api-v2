package fi.metatavu.timebank.api.controllers

import fi.metatavu.timebank.api.forecast.models.ForecastPerson
import fi.metatavu.timebank.api.persistence.model.WorktimeCalendar
import fi.metatavu.timebank.api.persistence.repositories.WorktimeCalendarRepository
import java.time.LocalDate
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

/**
 * Controller for WorktimeCalendars
 */
@ApplicationScoped
class WorktimeCalendarController {

    @Inject
    lateinit var worktimeCalendarRepository: WorktimeCalendarRepository

    /**
     * Gets WorktimeCalendar based on ID
     *
     * @param id id
     * @return WorktimeCalendar
     */
    suspend fun getWorktimeCalendar(id: UUID): WorktimeCalendar {
        return worktimeCalendarRepository.getWorktimeCalendar(id)
    }

    /**
     * Checks if persisted WorktimeCalendar is up-to-date for given person.
     * Returns most up-to-date WorktimeCalendar.
     * @param person person
     * @return WorktimeCalendar
     */
    suspend fun checkWorktimeCalendar(person: ForecastPerson): WorktimeCalendar {
        val worktimeCalendar = worktimeCalendarRepository.getUpToDateWorktimeCalendar(person.id)
            ?: createWorktimeCalendar(person)

        val updatedWorktimeCalendar = compareExpected(person, worktimeCalendar)
        return if (updatedWorktimeCalendar === worktimeCalendar) {
            worktimeCalendar
        } else {
            worktimeCalendarRepository.updateWorktimeCalendar(worktimeCalendar.id!!)
            updatedWorktimeCalendar
        }
    }

    /**
     * Compares most up-to-date WorktimeCalendar with
     * expected worktimes retrieved from Forecast.
     * Returns a new WorktimeCalendar if old one has ended.
     *
     * @param person Person
     * @param worktimeCalendar WorktimeCalendar
     * @return WorktimeCalendar
     */
    suspend fun compareExpected(person: ForecastPerson, worktimeCalendar: WorktimeCalendar): WorktimeCalendar {
        val isCalendarUpToDate =
            person.monday == worktimeCalendar.monday &&
            person.tuesday == worktimeCalendar.tuesday &&
            person.wednesday == worktimeCalendar.wednesday &&
            person.thursday == worktimeCalendar.thursday &&
            person.friday == worktimeCalendar.friday &&
            person.saturday == worktimeCalendar.saturday &&
            person.sunday == worktimeCalendar.sunday
        return if (isCalendarUpToDate) {
            worktimeCalendar
        } else {
            createWorktimeCalendar(person)
        }
    }

    /**
     * Creates new WorktimeCalendar for given person
     *
     * @param person Person
     * @return WorktimeCalendar
     */
    suspend fun createWorktimeCalendar(person: ForecastPerson): WorktimeCalendar {
        val newWorktimeCalendar = WorktimeCalendar(
            id = UUID.randomUUID(),
            personId = person.id,
            monday = person.monday,
            tuesday = person.tuesday,
            wednesday = person.wednesday,
            thursday = person.thursday,
            friday = person.friday,
            saturday = person.saturday,
            sunday = person.sunday,
            calendarStart = LocalDate.now()
        )
        worktimeCalendarRepository.persistWorktimeCalendar(newWorktimeCalendar)
        return newWorktimeCalendar
    }
}