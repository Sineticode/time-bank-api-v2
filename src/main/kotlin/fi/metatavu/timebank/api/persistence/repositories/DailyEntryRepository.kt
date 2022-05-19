package fi.metatavu.timebank.api.persistence.repositories

import fi.metatavu.timebank.api.persistence.model.DailyEntry
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase
import io.smallrye.mutiny.coroutines.awaitSuspending
import java.util.UUID
import javax.enterprise.context.ApplicationScoped

/**
 * Manages DailyEntry JPA entity
 */
@ApplicationScoped
class DailyEntryRepository: PanacheRepositoryBase<DailyEntry, UUID> {

    /**
     * Lists all dailyEntries
     *
     * @return List of DailyEntries
     */
    suspend fun getAllEntries(): List<DailyEntry>? {
        return listAll().awaitSuspending()
    }

    /**
     * Lists all dailyEntries for given Person
     *
     * @param personId personId
     * @return List of DailyEntries
     */
    suspend fun getEntriesById(personId: Int): List<DailyEntry> {
        return find("person", personId).list<DailyEntry>().awaitSuspending()
    }
}