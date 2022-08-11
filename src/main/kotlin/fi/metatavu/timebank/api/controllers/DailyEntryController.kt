package fi.metatavu.timebank.api.controllers

import fi.metatavu.timebank.api.forecast.models.ForecastPerson
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
    lateinit var timeEntryController: TimeEntryController

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
     * @param vacation filter vacation days
     * @return List of DailyEntries
     */
    suspend fun list(personId: Int?, before: LocalDate?, after: LocalDate?, vacation: Boolean?): List<DailyEntry>? {
        return makeDailyEntries(
            personId = personId,
            before = before,
            after = after,
            vacation = vacation
        )
    }

    /**
     * Makes list of DailyEntries
     *
     * @param personId persons id in Forecast
     * @param before LocalDate to retrieve entries before given date
     * @param after LocalDate to retrieve entries after given date
     * @param vacation filter vacation days
     * @return List of DailyEntries
     */
    suspend fun makeDailyEntries(personId: Int?, before: LocalDate?, after: LocalDate?, vacation: Boolean?): List<DailyEntry>? {
        return try {
            val persons = personsController.getPersonsFromForecast()
            val holidays = personsController.getHolidaysFromForecast()

            val entries = timeEntryController.getEntries(
                personId = personId,
                before = before,
                after = after,
                vacation = vacation
            )

            if (entries.isEmpty()) return null

            val dailyEntries = mutableListOf<DailyEntry>()

            persons.forEach { person ->
                entries
                    .filter { timeEntry -> timeEntry.person == person.id }
                    .groupBy { it.date }.values.map { entriesOfDay ->
                        dailyEntries.add(calculateDailyEntries(
                            entriesOfDay = entriesOfDay,
                            person = person,
                            holidays = holidays
                        ))
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
     * @param entriesOfDay List of TimeEntries
     * @param person ForecastPerson
     * @param holidays List of LocalDate
     * @return DailyEntry
     */
    suspend fun calculateDailyEntries(entriesOfDay: List<TimeEntry>, person: ForecastPerson, holidays: List<LocalDate>): DailyEntry {
        var internalTime = 0
        var billableProjectTime = 0
        var nonBillableProjectTime = 0
        var date = LocalDate.now()
        val worktimeCalendar = entriesOfDay.first().worktimeCalendar ?:
            throw Error("Missing WorktimeCalendar in TimeEntry of person ${person.firstName} ${person.lastName}.")
        var personId = entriesOfDay.first().person!!
        val isVacation = entriesOfDay.any { it.isVacation!! }

        entriesOfDay.forEach{ entry ->
            internalTime += entry.internalTime ?: 0
            billableProjectTime += entry.billableProjectTime ?: 0
            nonBillableProjectTime += entry.nonBillableProjectTime ?: 0
            date = entry.date
            personId = entry.person!!
        }

        val expected = getDailyExpected(
            worktimeCalendar = worktimeCalendarController.getWorktimeCalendar(worktimeCalendar.id!!),
            holidays = holidays,
            day = date
        )

        val loggedProjectTime = billableProjectTime + nonBillableProjectTime

        return DailyEntry(
            person = personId,
            internalTime = internalTime,
            billableProjectTime = billableProjectTime,
            nonBillableProjectTime = nonBillableProjectTime,
            logged = internalTime + loggedProjectTime,
            loggedProjectTime = loggedProjectTime,
            expected = expected,
            balance = internalTime + loggedProjectTime - expected,
            date = date,
            isVacation = isVacation
        )
    }

    /**
     * Gets persons expected workhours (in minutes) per day
     *
     * @param worktimeCalendar WorkTimeCalendar
     * @param holidays List of LocalDate
     * @return Int minutes of expected work
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