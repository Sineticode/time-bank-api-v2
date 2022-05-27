package fi.metatavu.timebank.api.utils

import fi.metatavu.timebank.model.Timespan

/**
 * Class for shared functions
 */
class GenericFunctions {

    /**
     * Totals the times for a timespan group
     *
     * @param groupBy timespanGroup class to organise the various grouping types
     * @param timespan span of time to be summed (from query param)
     * @param daily boolean distinguish between dailyEntry or persons total
     * @param personId personId
     * @return TimespanGroup with summed times for the given timespan
     */
    fun sumGroup(groupBy: TimespanGroup, timespan: Timespan, daily: Boolean, personId: Int?): TimespanGroup {
        if (daily) {
            if (personId != null) {
                groupBy.dayMap = groupBy.day?.reduce { _, sum, element ->
                    sum.internalTime = sum.internalTime?.plus(element.internalTime!!)
                    sum.projectTime = sum.projectTime?.plus(element.projectTime!!)
                    sum
                }
            } else {
                groupBy.allPersonDayMap = groupBy.allPersonDay?.reduce { _, sum, element ->
                    sum.internalTime = sum.internalTime?.plus(element.internalTime!!)
                    sum.projectTime = sum.projectTime?.plus(element.projectTime!!)
                    sum
                }
            }
        }
        else {
            when (timespan) {
                Timespan.ALL_TIME -> {
                    groupBy.allTimeMap = groupBy.allTime?.reduce { sum, element ->
                        sum.internalTime = sum.internalTime?.plus(element.internalTime!!)
                        sum.projectTime = sum.projectTime?.plus(element.projectTime!!)
                        sum
                    }
                }
                Timespan.YEAR -> {
                    groupBy.yearMap = groupBy.year?.reduce { _, sum, element ->
                        sum.internalTime = sum.internalTime?.plus(element.internalTime!!)
                        sum.projectTime = sum.projectTime?.plus(element.projectTime!!)
                        sum
                    }
                }
                Timespan.MONTH -> {
                    groupBy.weekMonthMap = groupBy.weekMonth?.reduce { _, sum, element ->
                        sum.internalTime = sum.internalTime?.plus(element.internalTime!!)
                        sum.projectTime = sum.projectTime?.plus(element.projectTime!!)
                        sum
                    }
                }
                Timespan.WEEK -> {
                    groupBy.weekMonthMap = groupBy.weekMonth?.reduce { _, sum, element ->
                        sum.internalTime = sum.internalTime?.plus(element.internalTime!!)
                        sum.projectTime = sum.projectTime?.plus(element.projectTime!!)
                        sum
                    }
                }
            }
        }
        return groupBy
    }
}