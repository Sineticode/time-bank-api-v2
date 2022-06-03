package fi.metatavu.timebank.api.controllers

import fi.metatavu.timebank.api.forecast.models.ForecastPerson
import fi.metatavu.timebank.api.persistence.repositories.TimeEntryRepository
import fi.metatavu.timebank.api.persistence.model.TimeEntry
import java.time.LocalDate
import fi.metatavu.timebank.model.DailyEntry
import java.time.DayOfWeek
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

/**
 * Controller for DailyEntry objects
 */
@ApplicationScoped
class DailyEntryController {

    @Inject
    lateinit var timeEntryRepository: TimeEntryRepository

    @Inject
    lateinit var personsController: PersonsController

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
        val persons = personsController.getPersonsFromForecast()
        val holidays = personsController.getHolidaysFromForecast()

        if (persons.isNullOrEmpty() || holidays.isNullOrEmpty()) {
            return null
        }

        val entries = timeEntryRepository.getAllEntries(
            personId = personId,
            before = before,
            after = after
        )

        if (entries.isEmpty()) return null

        val dailyEntries = mutableListOf<DailyEntry>()

        persons.forEach{ person ->
            entries
                .filter{ timeEntry -> timeEntry.person == person.id }
                .groupBy{ it.date }.values.map{ day ->
                    dailyEntries.add(calculateDailyEntries(day, person, holidays))
                }
        }

        return dailyEntries.sortedByDescending{ it.date }
    }

    /**
     * Totals TimeEntries to DailyEntries
     *
     * @param entries List of TimeEntries
     * @param person ForecastPerson
     * @param holidays List of LocalDate
     * @return DailyEntry
     */
    suspend fun calculateDailyEntries(entries: List<TimeEntry>, person: ForecastPerson, holidays: List<LocalDate>): DailyEntry {
        var internalTime = 0
        var projectTime = 0
        var date = LocalDate.now()

        entries.forEach{ entry ->
            internalTime += entry.internalTime ?: 0
            projectTime += entry.projectTime ?: 0
            date = entry.date
        }

        val expected = getDailyExpected(
            person = person,
            holidays = holidays,
            day = date
        )

        return DailyEntry(
            person = person.id,
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
    private suspend fun getDailyExpected(person: ForecastPerson, holidays: List<LocalDate>, day: LocalDate): Int {
        if (holidays.contains(day)) return 0

        return when (day.dayOfWeek) {
            DayOfWeek.MONDAY -> person.monday
            DayOfWeek.TUESDAY-> person.tuesday
            DayOfWeek.WEDNESDAY -> person.wednesday
            DayOfWeek.THURSDAY -> person.thursday
            DayOfWeek.FRIDAY -> person.friday
            DayOfWeek.SATURDAY -> person.saturday
            DayOfWeek.SUNDAY -> person.sunday
            else -> throw Error("An unexpected date!")
        }
    }
}