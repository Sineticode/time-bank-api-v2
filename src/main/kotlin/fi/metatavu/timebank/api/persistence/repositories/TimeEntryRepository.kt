package fi.metatavu.timebank.api.persistence.repositories

import fi.metatavu.timebank.api.persistence.model.TimeEntry
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase
import io.smallrye.mutiny.coroutines.awaitSuspending
import java.util.UUID
import javax.enterprise.context.ApplicationScoped

/**
 * Manages DailyEntry JPA entity
 */
@ApplicationScoped
class TimeEntryRepository: PanacheRepositoryBase<TimeEntry, UUID> {

    /**
     * Lists all dailyEntries
     *
     * @return List of DailyEntries
     */
    suspend fun getAllEntries(): List<TimeEntry>? {
        return listAll().awaitSuspending()
    }

    /**
     * Lists all TimeEntries for given Person
     *
     * @param personId personId
     * @return List of TimeEntries
     */
    suspend fun getEntriesByPersonId(personId: Int): List<TimeEntry> {
        return find("person", personId).list<TimeEntry>().awaitSuspending()
    }
}