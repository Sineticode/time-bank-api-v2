package fi.metatavu.timebank.api.persistence.repositories

import fi.metatavu.timebank.api.persistence.model.TimeEntry

import io.quarkus.hibernate.reactive.panache.Panache
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase
import io.smallrye.mutiny.coroutines.awaitSuspending
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneId
import java.util.UUID
import javax.enterprise.context.ApplicationScoped

/**
 * Manages TimeEntry JPA entity
 */
@ApplicationScoped
class TimeEntryRepository: PanacheRepositoryBase<TimeEntry, UUID> {

    /**
     * Lists all dailyEntries
     *
     * @return List of DailyEntries
     */
    suspend fun getAllEntries(personId: Int? = null, before: LocalDate? = null, after: LocalDate? = null): List<TimeEntry> {
        var queryString: String? = null
        val beforeOdt: OffsetDateTime? = before?.atStartOfDay(ZoneId.of("Europe/Helsinki"))?.toOffsetDateTime()
        val afterOdt: OffsetDateTime? = after?.atStartOfDay(ZoneId.of("Europe/Helsinki"))?.toOffsetDateTime()
        if (personId != null && before == null && after == null) {
            queryString = "person = $personId order by date DESC"
        }
        else if (personId == null &&  before != null && after == null) {
            queryString = "date < '$beforeOdt' order by date DESC"
        }
        else if (personId == null && before == null && after != null) {
            queryString = "date > '$afterOdt' order by date DESC"
        }
        else if (personId == null && before != null) {
            queryString = "date < '$beforeOdt' and date > '$afterOdt' order by date DESC"
        }
        else if (personId != null && before != null && after == null) {
            queryString = "person = $personId and date < '$beforeOdt' order by date DESC"
        }
        else if (personId != null && before == null) {
            queryString = "person = $personId and date > '$afterOdt' order by date DESC"
        }
        else if (personId != null) {
            queryString = "person = $personId and date < '$beforeOdt' and date > '$afterOdt' order by date DESC"
        }
        if (queryString != null) return find(queryString).list<TimeEntry>().awaitSuspending()
        return list("order by date DESC").awaitSuspending()
    }

    /**
     * Persists new TimeEntry
     *
     * @return 1 for persisted 0 for not persisted
     */
    suspend fun persistEntry(entry: TimeEntry): Int {
        if (find("forecastId", entry.forecastId).list<TimeEntry>().awaitSuspending().isEmpty()) {
            Panache.withTransaction { persist(entry) }.awaitSuspending()
            return 1
        }
        else if (entry.updatedAt!! > entry.createdAt!! && entry.updatedAt!! >= OffsetDateTime.now().minusDays(1)) {
            deleteEntry(entry.forecastId ?: 1)
            Panache.withTransaction { persist(entry) }.awaitSuspending()
            return 1
        }
        return 0
    }

    /**
     * Deletes persisted TimeEntry
     */
    suspend fun deleteEntry(forecastId: Int) {
        Panache.withTransaction { delete("forecastId", forecastId) }.awaitSuspending()
    }
}