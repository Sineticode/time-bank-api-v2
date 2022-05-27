package fi.metatavu.timebank.api.controllers

import fi.metatavu.timebank.api.persistence.repositories.TimeEntryRepository
import fi.metatavu.timebank.api.persistence.model.TimeEntry
import java.time.LocalDate
import fi.metatavu.timebank.api.persistence.repositories.TimeEntryRepository
import fi.metatavu.timebank.api.persistence.model.TimeEntry
import fi.metatavu.timebank.api.utils.GenericFunctions
import fi.metatavu.timebank.api.utils.TimespanGroup
import fi.metatavu.timebank.model.DailyEntry
import fi.metatavu.timebank.model.Timespan
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

/**
 * Controller for DailyEntry objects
 */
@ApplicationScoped
class DailyEntryController {

    @Inject
    lateinit var timeEntryRepository: TimeEntryRepository

    suspend fun list(personId: Int?, before: LocalDate?, after: LocalDate?): List<TimeEntry>? {
        return timeEntryRepository.getAllEntries(personId, before, after)
    }

    /**
     * Lists daily total times from combined timeEntries
     *
     * @param personId personId
     * @param timespan span of time to be summed (from query param)
     * @return List of DailyEntries
     */
    suspend fun getDailyTotal(personId: Int?, timespan: Timespan): MutableList<DailyEntry> {
        return calculateDailyEntry(personId, timespan)
    }

    /**
     * Calculates the dailyEntries from timeEntries
     *
     * @param personId personId
     * @param timespan span of time to be summed (from query param)
     * @return list of DailyEntries
     */
    private suspend fun calculateDailyEntry(personId: Int?, timespan: Timespan): MutableList<DailyEntry> {
        val personEntries: List<TimeEntry>?
        var dailyEntry: MutableList<DailyEntry>
        var timespanGroup = TimespanGroup()
        val genericFunctions = GenericFunctions()

        if (personId != null) {
            personEntries = timeEntryRepository.getEntriesByPersonId(personId)
            timespanGroup.day = personEntries.groupingBy { it.date }
            val dailyTotals = genericFunctions.sumGroup(timespanGroup, timespan, true, personId)
            dailyEntry = makeDailyEntry(dailyTotals, personId)
        } else {
            personEntries = timeEntryRepository.getAllEntries()
            timespanGroup.allPersonDay = personEntries?.groupingBy { Pair(it.person, it.date) }
            val allPersonDailyTotals = genericFunctions.sumGroup(timespanGroup, timespan, true, personId)
            dailyEntry = makeDailyEntry(allPersonDailyTotals, personId)
        }
        return dailyEntry
    }

    /**
     * Converts values from the TimespanGroup into DailyEntry type
     *
     * @param totals timespanGroup class to organise the various grouping types
     * @param personId personId
     * @return list of DailyEntries
     */
    private fun makeDailyEntry(totals: TimespanGroup, personId: Int?): MutableList<DailyEntry> {
        var totalList = mutableListOf<DailyEntry>()

        if (personId != null) {
            totals.dayMap?.forEach { i ->
                totalList.add(
                    DailyEntry(
                        person = i.value.person!!,
                        balance = 435 - (i.value.internalTime ?: 0) + (i.value.projectTime ?: 0),
                        logged = (i.value.internalTime ?: 0) + (i.value.projectTime ?: 0),
                        expected = 435,
                        internalTime = i.value.internalTime ?: 0,
                        projectTime = i.value.projectTime ?: 0,
                        date = i.value.date!!,
                    ))
            }
        } else {
            totals.allPersonDayMap?.forEach { i ->
                totalList.add(
                    DailyEntry(
                        person = i.value.person!!,
                        balance = 435 - (i.value.internalTime ?: 0) + (i.value.projectTime ?: 0),
                        logged = (i.value.internalTime ?: 0) + (i.value.projectTime ?: 0),
                        expected = 435,
                        internalTime = i.value.internalTime ?: 0,
                        projectTime = i.value.projectTime ?: 0,
                        date = i.value.date!!,
                    ))
            }
        }
        return totalList
    }
}