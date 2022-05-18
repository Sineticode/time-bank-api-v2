package fi.metatavu.timebank.api.impl.translate

import fi.metatavu.timebank.api.persistence.model.DailyEntry
import java.time.LocalDate
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class DailyEntryTranslator {

    suspend fun translateTimeEntry(timeEntries: Array<ForecastTimeEntry>): List<DailyEntry> {
        val translatedDailyEntries: MutableList<DailyEntry> = mutableListOf()
        timeEntries.map { i ->
            val translatedDailyEntry = DailyEntry()
            translatedDailyEntry.person = i.person
            if (i.non_project_time != null) {
                translatedDailyEntry.internalTime = i.time_registered
            }
            else translatedDailyEntry.projectTime = i.time_registered
            translatedDailyEntry.logged = i.time_registered
            translatedDailyEntry.expected = 435
            translatedDailyEntry.total = i.time_registered
            translatedDailyEntry.date = LocalDate.parse(i.date)
            translatedDailyEntries.add(translatedDailyEntry)
        }
        return translatedDailyEntries
    }

}

data class ForecastTimeEntry(
    val id: Int,
    val person: Int,
    val project: Int?,
    val card: Int?,
    val task: Int?,
    val non_project_time: Int?,
    val time_registered: Int,
    val date: String,
    val notes: String?,
    val created_by: Int,
    val updated_by: Int,
    val created_at: String,
    val updated_at: String
)