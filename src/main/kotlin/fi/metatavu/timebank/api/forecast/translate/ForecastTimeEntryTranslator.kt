package fi.metatavu.timebank.api.forecast.translate

import fi.metatavu.timebank.api.forecast.models.ForecastTimeEntry
import fi.metatavu.timebank.api.persistence.model.TimeEntry
import fi.metatavu.timebank.api.persistence.model.WorktimeCalendar
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
    fun translate(entity: ForecastTimeEntry, worktimeCalendars: List<WorktimeCalendar>): TimeEntry {
        val createdAt = LocalDateTime.parse(entity.createdAt.replace("Z", ""))
        val updatedAt = LocalDateTime.parse(entity.updatedAt.replace("Z", ""))
        val translatedTimeEntry = TimeEntry()
        translatedTimeEntry.entryId = UUID.randomUUID()
        translatedTimeEntry.forecastId = entity.id
        translatedTimeEntry.person = entity.person
        translatedTimeEntry.internalTime = if (entity.nonProjectTime != null) entity.timeRegistered else 0
        translatedTimeEntry.projectTime = if (entity.nonProjectTime != null) 0 else entity.timeRegistered
        translatedTimeEntry.date = LocalDate.parse(entity.date)
        translatedTimeEntry.createdAt = createdAt.atZone(ZoneId.of("Europe/Helsinki")).toOffsetDateTime()
        translatedTimeEntry.updatedAt = updatedAt.atZone(ZoneId.of("Europe/Helsinki")).toOffsetDateTime()
        translatedTimeEntry.worktimeCalendarId = worktimeCalendars.find { it.personId == entity.person }?.id
        return translatedTimeEntry
    }

    /**
     * Translates list of ForecastTimeEntries
     *
     * @param entities list of ForecastTimeEntries to translate
     * @return List of TimeEntries
     */
    fun translate(entities: List<ForecastTimeEntry>, worktimeCalendars: List<WorktimeCalendar>): List<TimeEntry> {
        return entities.map { entity ->
            translate(entity, worktimeCalendars)
        }
    }
}