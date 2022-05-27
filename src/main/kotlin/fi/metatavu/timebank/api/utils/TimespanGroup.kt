package fi.metatavu.timebank.api.utils

import fi.metatavu.timebank.api.persistence.model.TimeEntry
import java.time.LocalDate

/**
 * Class to allow various date grouping types to share functionality
 */
class TimespanGroup {

    var weekMonth: Grouping<TimeEntry, Pair<Int?, Int?>>? = null
    var weekMonthMap: Map<Pair<Int?, Int?>, TimeEntry>? = null
    var year:  Grouping<TimeEntry, Int?>? = null
    var yearMap: Map<Int?, TimeEntry>? = null
    var day:  Grouping<TimeEntry, LocalDate?>?  = null
    var dayMap:  Map<LocalDate?, TimeEntry>?  = null
    var allPersonDay: Grouping<TimeEntry, Pair<Int?, LocalDate?>>? = null
    var allPersonDayMap: Map<Pair<Int?, LocalDate?>, TimeEntry>? = null
    var allTime:  List<TimeEntry>? = null
    var allTimeMap:  Any? = null
}