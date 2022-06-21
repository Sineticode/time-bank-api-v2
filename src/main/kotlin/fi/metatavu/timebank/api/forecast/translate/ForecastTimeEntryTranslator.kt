package fi.metatavu.timebank.api.forecast.translate

import fi.metatavu.timebank.api.forecast.models.ForecastTimeEntry
import fi.metatavu.timebank.api.persistence.model.TimeEntry
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.*
import javax.enterprise.context.ApplicationScoped

/**
 * Translates ForecastTimeEntry object to persistable TimeEntry object
 */
@ApplicationScoped
class ForecastTimeEntryTranslator {

    /**
     * Translates ForecastTimeEntry into TimeEntry
     *
     * @param entity ForecastTimeEntry
     * @return TimeEntry
     */
    fun translate(entity: ForecastTimeEntry): TimeEntry {
        val translatedTimeEntry = TimeEntry()
        translatedTimeEntry.entryId = UUID.randomUUID()
        translatedTimeEntry.forecastId = entity.id
        translatedTimeEntry.person = entity.person
        translatedTimeEntry.internalTime = if (entity.nonProjectTime != null) entity.timeRegistered else 0
        translatedTimeEntry.projectTime = if (entity.nonProjectTime != null) 0 else entity.timeRegistered
        translatedTimeEntry.date = LocalDate.parse(entity.date)
        translatedTimeEntry.createdAt = OffsetDateTime.parse(entity.createdAt)
        translatedTimeEntry.updatedAt = OffsetDateTime.parse(entity.updatedAt)
        return translatedTimeEntry
    }

    /**
     * Translates list of ForecastTimeEntries
     *
     * @param entities list of ForecastTimeEntries to translate
     * @return List of TimeEntries
     */
    fun translate(entities: List<ForecastTimeEntry>): List<TimeEntry> {
        return entities.map(this::translate)
    }
}