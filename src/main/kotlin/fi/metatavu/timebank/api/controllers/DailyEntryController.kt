package fi.metatavu.timebank.api.controllers

import fi.metatavu.timebank.api.forecast.models.ForecastPerson
import fi.metatavu.timebank.api.persistence.repositories.TimeEntryRepository
import fi.metatavu.timebank.api.persistence.model.TimeEntry
import java.time.LocalDate
import fi.metatavu.timebank.model.DailyEntry
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
     * @param personId personId
     * @param timespan span of time to be summed (from query param)
     * @return List of DailyEntries
     */
    suspend fun list(personId: Int?, before: LocalDate?, after: LocalDate?): List<DailyEntry> {
        return makeDailyEntries(personId, before, after)
    }

    /**
     * Makes list of DailyEntries
     *
     * @param personId personId
     * @param before LocalDate
     * @param after LocalDate
     * @return List of DailyEntries
     */
    suspend fun makeDailyEntries(personId: Int? = null, before: LocalDate? = null, after: LocalDate? = null): List<DailyEntry> {
        val persons = personsController.getPersonsFromForecast()
        val holidays = personsController.getHolidaysFromForecast()
        val entries = timeEntryRepository.getAllEntries(personId, before, after)
        val dailyEntries = mutableListOf<DailyEntry>()
        if (personId != null) {
            val person = persons.find{ person -> person.id == personId }
            val personEntries = entries.filter{ timeEntry -> timeEntry.person == person!!.id }
            personEntries.groupBy { it.date }.values.toList().forEach{ day ->
                dailyEntries.add(calculateDailyEntries(day, person!!, holidays))
            }
        }
        else {
            persons.map{ person ->
                entries
                    .filter{ timeEntry -> timeEntry.person == person.id }
                    .groupBy{ it.date }.values.toList().forEach{ day ->
                        dailyEntries.add(calculateDailyEntries(day, person, holidays))
                    }
            }
        }
        return dailyEntries.sortedByDescending { it.date }
    }

    /**
     * Totals TimeEntries to DailyEntries
     *
     * @param List of TimeEntries
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
            person,
            holidays,
            date
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
    private suspend fun getDailyExpected(person: ForecastPerson?, holidays: List<LocalDate>, day: LocalDate): Int {
        if (holidays.contains(day)) return 0
        if (day.dayOfWeek.toString() == "SATURDAY") person?.saturday
        else if (day.dayOfWeek.toString() == "SUNDAY") person?.sunday
        else return 435
        return 0
    }
}