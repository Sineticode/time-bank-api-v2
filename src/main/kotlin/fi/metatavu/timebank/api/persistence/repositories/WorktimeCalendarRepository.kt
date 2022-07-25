package fi.metatavu.timebank.api.persistence.repositories

import fi.metatavu.timebank.api.persistence.model.WorktimeCalendar
import io.quarkus.hibernate.reactive.panache.Panache
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
     * Gets most up-to-date WorktimeCalendars for given person
     *
     * @param personId person id
     * @return WorktimeCalendars
     */
    suspend fun getUpToDateWorktimeCalendar(personId: Int): WorktimeCalendar? {
        return try {
            find("personId = ?1 AND calendarEnd = NULL", personId).singleResult<WorktimeCalendar?>().awaitSuspending()
        } catch (ex: Exception) {
            null
        }
    }

    /**
     * Gets WorktimeCalendar based on id
     *
     * @param id id
     * @return WorktimeCalendar
     */
    suspend fun getWorktimeCalendar(id: UUID): WorktimeCalendar {
        return findById(id).awaitSuspending()
    }

    /**
     * Updates persisted WorktimeCalendar
     *
     * @param id id
     */
    suspend fun updateWorktimeCalendar(id: UUID) {
        Panache.withTransaction {
            update(
                "calendarEnd = ?1 WHERE id = ?2",
                LocalDate.now().minusDays(1), id
            )
        }.awaitSuspending()
    }

    /**
     * Persists new WorktimeCalendar
     *
     * @param worktimeCalendar WorktimeCalendar
     * @return worktimeCalendar WorktimeCalendar
     */
    suspend fun persistWorktimeCalendar(worktimeCalendar: WorktimeCalendar){
        Panache.withTransaction {
            persist(worktimeCalendar)
        }.awaitSuspending()
    }
}