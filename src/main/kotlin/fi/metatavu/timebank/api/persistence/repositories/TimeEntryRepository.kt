package fi.metatavu.timebank.api.persistence.repositories

import fi.metatavu.timebank.api.persistence.model.TimeEntry
import io.quarkus.hibernate.reactive.panache.Panache
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase
import io.quarkus.panache.common.Parameters
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
     * Lists all timeEntries
     *
     * @param personId persons id in Forecast
     * @param before LocalDate to retrieve entries before given date
     * @param after LocalDate to retrieve entries after given date
     * @return List of timeEntries
     */
    suspend fun getAllEntries(personId: Int?, before: LocalDate?, after: LocalDate?): List<TimeEntry> {
        println("TEST IS HERE")
        val stringBuilder = StringBuilder()
        val parameters = Parameters()

        if (personId != null) {
            stringBuilder.append("person = :personId")
            parameters.and("personId", personId)
        }

        if (before != null) {
            stringBuilder.append(if (stringBuilder.isNotEmpty()) " and date < :before" else "date < :before")
            parameters.and("before", before)
        }

        if (after != null) {
            stringBuilder.append(if (stringBuilder.isNotEmpty()) " and date > :after" else "date > :after")
            parameters.and("after", after)
        }

        stringBuilder.append(" order by date DESC")

        return find(stringBuilder.toString(), parameters).list<TimeEntry>().awaitSuspending()
    }

    /**
     * Persists new TimeEntry
     *
     * @param entry TimeEntry
     * @return true for persisted false for not persisted
     */
    suspend fun persistEntry(entry: TimeEntry): Boolean {

        if (find("forecastId", entry.forecastId).list<TimeEntry>().awaitSuspending().isEmpty()) {
            Panache.withTransaction { persist(entry) }.awaitSuspending()
            return true
        }

        if (entry.updatedAt!! > entry.createdAt!! && entry.updatedAt!! >= OffsetDateTime.now().minusDays(1)) {
            deleteEntry(entry.forecastId ?: 1)
            Panache.withTransaction { persist(entry) }.awaitSuspending()
            return true
        }

        return false
    }

    /**
     * Deletes persisted TimeEntry
     *
     * @param forecastId id of time registration in Forecast
     */
    suspend fun deleteEntry(forecastId: Int) {
        Panache.withTransaction { delete("forecastId", forecastId) }.awaitSuspending()
    }
}