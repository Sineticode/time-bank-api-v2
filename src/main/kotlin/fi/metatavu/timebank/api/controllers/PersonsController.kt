package fi.metatavu.timebank.api.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import fi.metatavu.timebank.model.PersonTotalTime
import fi.metatavu.timebank.api.forecast.ForecastService
import fi.metatavu.timebank.api.forecast.models.ForecastHoliday
import fi.metatavu.timebank.api.forecast.models.ForecastPerson
import org.slf4j.Logger
import fi.metatavu.timebank.model.DailyEntry
import fi.metatavu.timebank.model.Timespan
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.WeekFields
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

/**
 * Controller for Person objects
 */
@ApplicationScoped
class PersonsController {

    @Inject
    lateinit var forecastService: ForecastService

    @Inject
    lateinit var dailyEntryController: DailyEntryController

    @Inject
    lateinit var logger: Logger

    /**
     * List persons data from Forecast API
     *
     * @return List of ForecastPersons
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
     * List national holidays from Forecast API
     *
     * @return List of LocalDate
     */
    suspend fun getHolidaysFromForecast(): List<LocalDate> {
        return try {
            val resultString = forecastService.getHolidays()
            val forecastHolidays = jacksonObjectMapper().readValue(resultString, Array<ForecastHoliday>::class.java).toList()
            val holidays = mutableListOf<LocalDate>()
            forecastHolidays.map{ holiday ->
                holidays.add(LocalDate.of(holiday.year, holiday.month, holiday.day))
            }
            holidays
        } catch (e: Error) {
            logger.error("Error when executing get request: ${e.localizedMessage}")
            throw Error(e.localizedMessage)
        }
    }

    /**
     * Filters inactive Forecast persons and system users
     *
     * @param persons List of ForecastPersons
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
     * Makes List of PersonTotalTimes
     *
     * @param personId personId
     * @param timespan Timespan
     * @return List of PersonTotalTimes
     */
    suspend fun makePersonTotal(personId: Int, timespan: Timespan): List<PersonTotalTime>? {
        val dailyEntries = dailyEntryController.makeDailyEntries(personId)
        if (dailyEntries.isEmpty()) return null
        val personTotalTimeList = mutableListOf<PersonTotalTime>()
        when (timespan) {
            Timespan.ALL_TIME -> {
                personTotalTimeList.add(calculatePersonTotalTime(personId, dailyEntries, timespan))
            }
            Timespan.YEAR -> {
                dailyEntries.groupBy{ it.date.year }.values.toList().forEach{ year ->
                    personTotalTimeList.add(calculatePersonTotalTime(personId, year, timespan))
                }
            }
            Timespan.MONTH -> {
                dailyEntries.groupBy{ Pair(it.date.year, it.date.monthValue) }.values.toList().forEach{ month ->
                    personTotalTimeList.add(calculatePersonTotalTime(personId, month, timespan))
                }
            }
            Timespan.WEEK -> {
                val weekOfYear = WeekFields.of(DayOfWeek.MONDAY, 7).weekOfYear()
                dailyEntries.groupBy{ Pair(it.date.year, it.date.get(weekOfYear)) }.values.toList().forEach{ week ->
                    personTotalTimeList.add(calculatePersonTotalTime(personId,week, timespan))
                }
            }
        }
        return personTotalTimeList
    }

    /**
     * Totals DailyEntries to PersonTotalTimes based on timespan
     *
     * @param personId personId
     * @param days List of DailyEntries
     * @param timespan timespan
     * @return PersonTotalTime
     */
    private suspend fun calculatePersonTotalTime(personId: Int, days: List<DailyEntry>, timespan: Timespan): PersonTotalTime {
        val weekOfYear = WeekFields.of(DayOfWeek.MONDAY, 7).weekOfYear()
        var internalTime = 0
        var projectTime = 0
        var expected = 0
        var year: Int? = null
        var month: Int? = null
        var week: Int? = null
        days.forEach{ day ->
            internalTime += day.internalTime
            projectTime += day.projectTime
            expected += day.expected
            year = if (timespan != Timespan.ALL_TIME) day.date.year else null
            month = if (timespan == Timespan.MONTH || timespan == Timespan.WEEK) day.date.monthValue else null
            week = if (timespan == Timespan.WEEK) day.date.get(weekOfYear) else null
        }
        return PersonTotalTime(
            balance = internalTime + projectTime - expected,
            logged = internalTime + projectTime,
            expected = expected,
            internalTime = internalTime,
            projectTime = projectTime,
            personId = personId,
            year = year,
            monthNumber = month,
            weekNumber = week
        )
    }
}