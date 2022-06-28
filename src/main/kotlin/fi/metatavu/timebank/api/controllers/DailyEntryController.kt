package fi.metatavu.timebank.api.controllers

import fi.metatavu.timebank.api.persistence.repositories.TimeEntryRepository
import fi.metatavu.timebank.api.persistence.model.TimeEntry
import fi.metatavu.timebank.api.persistence.model.WorktimeCalendar
import java.time.LocalDate
import fi.metatavu.timebank.model.DailyEntry
import org.slf4j.Logger
import java.time.DayOfWeek
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

/**
 * Controller for DailyEntry objects
 */
@ApplicationScoped
class DailyEntryController {

    @Inject
    lateinit var worktimeCalendarController: WorktimeCalendarController

    @Inject
    lateinit var timeEntryRepository: TimeEntryRepository

    @Inject
    lateinit var personsController: PersonsController

    @Inject
    lateinit var logger: Logger

    /**
     * Lists daily total times from combined timeEntries
     *
     * @param personId persons id in Forecast
     * @param before LocalDate to retrieve entries before given date
     * @param after LocalDate to retrieve entries after given date
     * @return List of DailyEntries
     */
    suspend fun list(personId: Int?, before: LocalDate?, after: LocalDate?): List<DailyEntry>? {
        return makeDailyEntries(personId, before, after)
    }

    /**
     * Makes list of DailyEntries
     *
     * @param personId persons id in Forecast
     * @param before LocalDate to retrieve entries before given date
     * @param after LocalDate to retrieve entries after given date
     * @return List of DailyEntries
     */
    suspend fun makeDailyEntries(personId: Int?, before: LocalDate?, after: LocalDate?): List<DailyEntry>? {
        return try {
            val persons = personsController.getPersonsFromForecast()
            val holidays = personsController.getHolidaysFromForecast()
            val worktimeCalendars = persons.map { person ->
                worktimeCalendarController.checkWorktimeCalendar(person)
            }

            val entries = timeEntryRepository.getAllEntries(
                personId = personId,
                before = before,
                after = after
            )

            if (entries.isEmpty()) return null

            val dailyEntries = mutableListOf<DailyEntry>()

            worktimeCalendars.forEach { worktimeCalendar ->
                entries
                    .filter { timeEntry -> timeEntry.person == worktimeCalendar.personId }
                    .groupBy { it.date }.values.map { day ->
                        dailyEntries.add(calculateDailyEntries(day, worktimeCalendar, holidays))
                    }
            }

            dailyEntries.sortedByDescending { it.date }
        } catch (e: Error) {
            logger.error("Error during calculation of daily entries: ${e.localizedMessage}")
            throw Error(e.localizedMessage)
        }
    }

    /**
     * Totals TimeEntries to DailyEntries
     *
     * @param entries List of TimeEntries
     * @param person ForecastPerson
     * @param holidays List of LocalDate
     * @return DailyEntry
     */
    suspend fun calculateDailyEntries(entries: List<TimeEntry>, worktimeCalendar: WorktimeCalendar, holidays: List<LocalDate>): DailyEntry {
        var internalTime = 0
        var projectTime = 0
        var date = LocalDate.now()

        entries.forEach{ entry ->
            internalTime += entry.internalTime ?: 0
            projectTime += entry.projectTime ?: 0
            date = entry.date
        }

        val expected = getDailyExpected(
            worktimeCalendar = worktimeCalendar,
            holidays = holidays,
            day = date
        )

        return DailyEntry(
            person = worktimeCalendar.personId!!,
            internalTime = internalTime,
            projectTime = projectTime,
            logged = internalTime + projectTime,
            expected = expected,
            balance = internalTime + projectTime - expected,
            date = date
        )
    }

    /**
     * Gets persons expected workhours (in minutes) per day
     *
     * @param person ForecastPerson
     * @param holidays List of LocalDate
     * @return int minutes of expected work
     */
    private suspend fun getDailyExpected(worktimeCalendar: WorktimeCalendar, holidays: List<LocalDate>, day: LocalDate): Int {
        if (holidays.contains(day)) return 0

        return when (day.dayOfWeek) {
            DayOfWeek.MONDAY -> worktimeCalendar.monday!!
            DayOfWeek.TUESDAY-> worktimeCalendar.tuesday!!
            DayOfWeek.WEDNESDAY -> worktimeCalendar.wednesday!!
            DayOfWeek.THURSDAY -> worktimeCalendar.thursday!!
            DayOfWeek.FRIDAY -> worktimeCalendar.friday!!
            DayOfWeek.SATURDAY -> worktimeCalendar.saturday!!
            DayOfWeek.SUNDAY -> worktimeCalendar.sunday!!
            else -> throw Error("An unexpected date!")
        }
    }
}