package fi.metatavu.timebank.api.persistence.repositories

import fi.metatavu.timebank.api.persistence.model.WorktimeCalendar
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase
import io.smallrye.mutiny.coroutines.awaitSuspending
import java.time.LocalDate
import java.util.*
import javax.enterprise.context.ApplicationScoped

/**
 * Manages WorktimeCalendar JPA entity
 */
@ApplicationScoped
class WorktimeCalendarRepository: PanacheRepositoryBase<WorktimeCalendar, UUID> {

    /**
     * Gets WorktimeCalendar for given person
     *
     * @param optional personId person id
     * @return WorktimeCalendar
     */
    suspend fun getWorktimeCalendars(personId: Int): WorktimeCalendar? {
        val worktimeCalendar = find("personId", personId)
                                .list<WorktimeCalendar>()
                                .awaitSuspending()
                                .filter { it.calendarEnd == null }
        return if (worktimeCalendar.isEmpty()) {
            null
        } else {
            worktimeCalendar.first()
        }
    }

    /**
     * Updates WorktimeCalendar
     *
     * @param id id
     */
    suspend fun updateWorktimeCalendar(id: UUID) {
        update("calendarEnd", LocalDate.now()).awaitSuspending()
    }

    /**
     * Persists new WorktimeCalendar
     *
     * @param worktimeCalendar WorktimeCalendar
     * @return worktimeCalendar WorktimeCalendar
     */
    suspend fun persistWorktimeCalendar(worktimeCalendar: WorktimeCalendar): WorktimeCalendar {
        return persist(worktimeCalendar).awaitSuspending()
    }
}