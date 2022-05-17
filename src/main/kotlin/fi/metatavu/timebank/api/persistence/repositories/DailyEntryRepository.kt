package fi.metatavu.timebank.api.persistence.repositories

import fi.metatavu.timebank.api.persistence.model.DailyEntry
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase
import io.quarkus.panache.common.Sort
import io.smallrye.mutiny.coroutines.awaitSuspending
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class DailyEntryRepository: PanacheRepositoryBase<DailyEntry, UUID> {

    suspend fun getAllEntries(): MutableList<DailyEntry>? {
        return listAll(Sort.by("person")).awaitSuspending()
    }

    suspend fun getEntriesById(personId: Int): List<DailyEntry> {
        return find("person", personId).list<DailyEntry>().awaitSuspending()
    }
}