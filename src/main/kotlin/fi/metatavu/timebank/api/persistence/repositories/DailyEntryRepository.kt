package fi.metatavu.timebank.api.persistence.repositories

import fi.metatavu.timebank.api.persistence.model.DailyEntry
import fi.metatavu.timebank.api.persistence.model.Person
import io.quarkus.hibernate.reactive.panache.Panache
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase
import io.quarkus.panache.common.Sort
import io.smallrye.mutiny.coroutines.awaitSuspending
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneId
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class DailyEntryRepository: PanacheRepositoryBase<DailyEntry, UUID> {

    suspend fun getAllEntries(personId: Int?, before: LocalDate?, after: LocalDate?): MutableList<DailyEntry> {
        var queryString: String? = null
        val beforeOdt: OffsetDateTime? = before?.atStartOfDay(ZoneId.of("Europe/Helsinki"))?.toOffsetDateTime()
        val afterOdt: OffsetDateTime? = after?.atStartOfDay(ZoneId.of("Europe/Helsinki"))?.toOffsetDateTime()
        if (personId != null && before == null && after == null) {
            queryString = "person = $personId"
        }
        else if (personId == null &&  before != null && after == null) {
            queryString = "date < '$beforeOdt'"
        }
        else if (personId == null && before == null && after != null) {
            queryString = "date > '$afterOdt'"
        }
        else if (personId == null && before != null) {
            queryString = "date < '$beforeOdt' and date > '$afterOdt'"
        }
        else if (personId != null && before != null && after == null) {
            queryString = "person = $personId and date < '$beforeOdt'"
        }
        else if (personId != null && before == null) {
            queryString = "person = $personId and date > '$afterOdt'"
        }
        else if (personId != null) {
            queryString = "person = $personId and date < '$beforeOdt' and date > '$afterOdt'"
        }
        if (queryString != null) return find(queryString).list<DailyEntry>().awaitSuspending()
        return listAll(Sort.by("date ASC")).awaitSuspending()
    }

    suspend fun getEntriesById(personId: Int): MutableList<DailyEntry> {
        return find("person", personId).list<DailyEntry>().awaitSuspending()
    }

    suspend fun synchronizeEntries(entries: List<DailyEntry>, persons: List<Person>): Int {
        entries.forEachIndexed { idx, timeEntry ->
            val personName = persons.find { person -> person.personId == timeEntry.person.toLong() }?.lastName + ", " + persons.find { person -> person.personId == timeEntry.person.toLong()}?.firstName
            println("Synchronizing DailyEntry of $personName (#${idx + 1})...")
            Panache.withTransaction { persist(timeEntry) }.awaitSuspending()
            println("Finished synchronizing DailyEntry #${idx + 1}!")
        }
        return entries.size
    }
}