package fi.metatavu.timebank.api.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import fi.metatavu.timebank.api.persistence.repositories.TimeEntryRepository
import fi.metatavu.timebank.model.PersonTotalTime
import fi.metatavu.timebank.api.forecast.ForecastService
import fi.metatavu.timebank.api.forecast.models.ForecastPerson
import org.slf4j.Logger
import fi.metatavu.timebank.api.persistence.model.TimeEntry
import fi.metatavu.timebank.api.utils.GenericFunctions
import fi.metatavu.timebank.api.utils.TimespanGroup
import fi.metatavu.timebank.model.Timespan
import java.time.DayOfWeek
import java.time.temporal.WeekFields
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

/**
 * Controller for Person objects
 */
@ApplicationScoped
class PersonsController {

    @Inject
    lateinit var timeEntryRepository: TimeEntryRepository

    @Inject
    lateinit var forecastService: ForecastService

    @Inject
    lateinit var logger: Logger

    /**
     * List persons data from Forecast API
     *
     * @return Array of forecastPersons
     */
    suspend fun getPersonsFromForecast(): List<ForecastPerson> {
        return try {
            val resultString = forecastService.getPersons()
            jacksonObjectMapper().readValue(resultString, Array<ForecastPerson>::class.java).toList()
        } catch (e: Error) {
            logger.error("Error when executing get request: ${e.localizedMessage}")
            throw Error(e.localizedMessage)
        }
    }

    /**
     * Filters inactive Forecast persons and system users
     *
     * @return List of Forecast persons
     */
    fun filterActivePersons(persons: List<ForecastPerson>): List<ForecastPerson> {
        return persons.filter{ person -> person.active && !person.is_system_user }
    }

    /**
     * Lists Forecast persons based on optional query parameters
     *
     * @param active
     * @return List of Forecast persons
     */
    suspend fun listPersons(active: Boolean?): List<ForecastPerson> {
        var persons = getPersonsFromForecast()
        if (active == true) persons = filterActivePersons(persons)
        return persons
    }

    /**
     * Lists persons total time
     *
     * @param personId personId
     * @param timespan span of time to be summed (from query param)
     * @return List of TimeEntries
     */
    suspend fun getPersonTotal(personId: Int, timespan: Timespan): MutableList<PersonTotalTime> {
        return calculatePersonTotalTime(personId, timespan)
    }

//    @Jari That I'm using 3 switches essentially in a row for the functions below smells suspicious to me, I tried to find a way around it but didn't. Would be great to speak about how to improve this with you.
    /**
     * Calculates the persons total time from timeEntries
     *
     * @param personId personId
     * @param timespan span of time to be summed (from query param)
     * @return list of Persons total times
     */
    private suspend fun calculatePersonTotalTime(personId: Int, timespan: Timespan): MutableList<PersonTotalTime> {
        val personEntries: List<TimeEntry> = timeEntryRepository.getAllEntries(personId)
        var totalTime = mutableListOf<PersonTotalTime>()
        val genericFunctions = GenericFunctions()
        val timespanGroup = TimespanGroup()

        val dayCount = personEntries.groupingBy { it.date }.eachCount().size

        when (timespan) {
            Timespan.ALL_TIME -> {
                timespanGroup.allTime = personEntries
                val allTotals = genericFunctions.sumGroup(timespanGroup, timespan, false, personId)
                totalTime = makePersonTotalTimeEntry(allTotals, timespan, dayCount)
            }
            Timespan.YEAR -> {
                timespanGroup.year = personEntries.groupingBy { it.date?.year }
                val yearTotals = genericFunctions.sumGroup(timespanGroup, timespan, false, personId)
                totalTime = makePersonTotalTimeEntry(yearTotals, timespan, dayCount)
            }
            Timespan.MONTH -> {
                timespanGroup.weekMonth = personEntries.groupingBy { Pair(it.date?.year, it.date?.monthValue) }
                val monthTotals = genericFunctions.sumGroup(timespanGroup, timespan, false, personId)
                totalTime = makePersonTotalTimeEntry(monthTotals, timespan, dayCount)
            }
            Timespan.WEEK -> {
                val weekOfYear = WeekFields.of(DayOfWeek.MONDAY, 7).weekOfYear()
                timespanGroup.weekMonth = personEntries.groupingBy { Pair(it.date?.year, it.date?.get(weekOfYear)) }
                val weekTotals = genericFunctions.sumGroup(timespanGroup, timespan, false, personId)
                totalTime = makePersonTotalTimeEntry(weekTotals, timespan, dayCount)
            }
        }
        return totalTime
    }

    /**
     * Converts values from the TimespanGroup into PersonTotalTime type
     *
     * @param totals timespanGroup class to organise the various grouping types
     * @param timespan span of time to be summed (from query param)
     * @return list of persons total times
     */
    private fun makePersonTotalTimeEntry(totals: TimespanGroup, timespan: Timespan, dayCount: Int): MutableList<PersonTotalTime> {
        var totalList = mutableListOf<PersonTotalTime>()
            when (timespan) {
                Timespan.ALL_TIME -> {
                    val allTime = totals.allTime?.get(0)
                    totalList.add(
                        PersonTotalTime(
                            balance = 435 - (allTime?.internalTime ?: 0) + (allTime?.projectTime ?: 0),
                            logged = (allTime?.internalTime ?: 0) + (allTime?.projectTime ?: 0),
                            expected = 435 * dayCount,
                            internalTime = allTime?.internalTime ?: 0,
                            projectTime = allTime?.projectTime ?: 0,
                            personId = allTime?.person!!,
                            year = null,
                            monthNumber = null,
                            weekNumber = null
                        ))
                }
                Timespan.YEAR -> {
                    totals.yearMap?.forEach { i ->
                        totalList.add(
                            PersonTotalTime(
                                balance = 435 - (i.value.internalTime ?: 0) + (i.value.projectTime ?: 0),
                                logged = (i.value.internalTime ?: 0) + (i.value.projectTime ?: 0),
                                expected = 435 * dayCount,
                                internalTime = i.value.internalTime ?: 0,
                                projectTime = i.value.projectTime ?: 0,
                                personId = i.value.person!!,
                                year = i.value.date?.year,
                                monthNumber = null,
                                weekNumber = null
                            ))
                    }
                }
                Timespan.MONTH -> {
                    totals.weekMonthMap?.forEach { i ->
                        totalList.add(
                            PersonTotalTime(
                                balance = 435 - (i.value.internalTime ?: 0) + (i.value.projectTime ?: 0),
                                logged = (i.value.internalTime ?: 0) + (i.value.projectTime ?: 0),
                                expected = 435 * dayCount,
                                internalTime = i.value.internalTime ?: 0,
                                projectTime = i.value.projectTime ?: 0,
                                personId = i.value.person!!,
                                year = i.value.date?.year,
                                monthNumber = i.value.date?.monthValue,
                                weekNumber = null
                            ))
                    }
                }
                Timespan.WEEK -> {
                    totals.weekMonthMap?.forEach { i ->
                        totalList.add(
                            PersonTotalTime(
                                balance = 435 - (i.value.internalTime ?: 0) + (i.value.projectTime ?: 0),
                                logged = (i.value.internalTime ?: 0) + (i.value.projectTime ?: 0),
                                expected = 435 * dayCount,
                                internalTime = i.value.internalTime ?: 0,
                                projectTime = i.value.projectTime ?: 0,
                                personId = i.value.person!!,
                                year = i.value.date?.year,
                                monthNumber = i.value.date?.monthValue,
                                weekNumber = i.value.date?.get(WeekFields.of(DayOfWeek.MONDAY, 7).weekOfYear())
                            ))
                    }
                }
            }
        return totalList
    }
}