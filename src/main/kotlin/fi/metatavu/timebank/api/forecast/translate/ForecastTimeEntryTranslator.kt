package fi.metatavu.timebank.api.forecast.translate

import fi.metatavu.timebank.api.forecast.models.ForecastTask
import fi.metatavu.timebank.api.forecast.models.ForecastTimeEntry
import fi.metatavu.timebank.api.persistence.model.TimeEntry
import fi.metatavu.timebank.api.persistence.model.WorktimeCalendar
import fi.metatavu.timebank.api.utils.VacationUtils
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
     * @param worktimeCalendars List of WorktimeCalendars
     * @param forecastProjects List of ForecastProjects
     * @param forecastTasks List of ForecastTasks
     * @return TimeEntry
     */
    fun translate(entity: ForecastTimeEntry, worktimeCalendars: List<WorktimeCalendar>, forecastTasks: List<ForecastTask>
    ): TimeEntry {
        val createdAt = LocalDateTime.parse(entity.createdAt.replace("Z", ""))
        val updatedAt = LocalDateTime.parse(entity.updatedAt.replace("Z", ""))
        val unBillableTask = forecastTasks.find { it.id == entity.task }?.unBillable ?: true
        val internalTime = entity.nonProjectTime != null
        val translatedTimeEntry = TimeEntry()
        translatedTimeEntry.entryId = UUID.randomUUID()
        translatedTimeEntry.forecastId = entity.id
        translatedTimeEntry.person = entity.person
        translatedTimeEntry.internalTime = if (internalTime) entity.timeRegistered else 0
        translatedTimeEntry.billableProjectTime = 0
        translatedTimeEntry.nonBillableProjectTime = 0

        if (!unBillableTask && !internalTime) {
            translatedTimeEntry.billableProjectTime = entity.timeRegistered
        } else if (unBillableTask && !internalTime) {
            translatedTimeEntry.nonBillableProjectTime = entity.timeRegistered
        }

        translatedTimeEntry.date = LocalDate.parse(entity.date)
        translatedTimeEntry.createdAt = createdAt.atZone(ZoneId.of("Europe/Helsinki")).toOffsetDateTime()
        translatedTimeEntry.updatedAt = updatedAt.atZone(ZoneId.of("Europe/Helsinki")).toOffsetDateTime()
        translatedTimeEntry.worktimeCalendar = worktimeCalendars.find { it.personId == entity.person }
        translatedTimeEntry.isVacation = entity.nonProjectTime == VacationUtils.VACATION_ID
        return translatedTimeEntry
    }

    /**
     * Translates list of ForecastTimeEntries
     *
     * @param entities list of ForecastTimeEntries to translate
     * @param worktimeCalendars List of WorktimeCalendars
     * @return List of TimeEntries
     */
    fun translate(entities: List<ForecastTimeEntry>, worktimeCalendars: List<WorktimeCalendar>, forecastTasks: List<ForecastTask>
    ): List<TimeEntry> {
        return entities.map { entity ->
            translate(entity, worktimeCalendars, forecastTasks)
        }
    }
}