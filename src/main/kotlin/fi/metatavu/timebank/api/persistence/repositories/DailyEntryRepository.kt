package fi.metatavu.timebank.api.persistence.repositories

import fi.metatavu.timebank.api.persistence.model.DailyEntry
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class DailyEntryRepository: PanacheRepository<DailyEntry> {

    fun getAllEntries(): List<DailyEntry> {
        return listAll()
    }

    fun getEntriesById(personId: Int): List<DailyEntry> {
        return find("person", personId).list()
    }
}