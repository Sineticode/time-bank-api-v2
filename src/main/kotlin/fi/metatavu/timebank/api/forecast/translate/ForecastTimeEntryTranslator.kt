package fi.metatavu.timebank.api.forecast.translate

import fi.metatavu.timebank.api.forecast.models.ForecastTimeEntry
import fi.metatavu.timebank.api.persistence.model.TimeEntry
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
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
        val createdAt = LocalDateTime.parse(entity.created_at.replace("Z", ""))
        val updatedAt = LocalDateTime.parse(entity.updated_at.replace("Z", ""))
        val translatedTimeEntry = TimeEntry()
        translatedTimeEntry.entryId = UUID.randomUUID()
        translatedTimeEntry.forecastId = entity.id
        translatedTimeEntry.person = entity.person
        translatedTimeEntry.internalTime = if (entity.non_project_time != null) entity.time_registered else 0
        translatedTimeEntry.projectTime = if (entity.non_project_time != null) 0 else entity.time_registered
        translatedTimeEntry.date = LocalDate.parse(entity.date)
        translatedTimeEntry.createdAt = createdAt.atZone(ZoneId.of("Europe/Helsinki")).toOffsetDateTime()
        translatedTimeEntry.updatedAt = updatedAt.atZone(ZoneId.of("Europe/Helsinki")).toOffsetDateTime()
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