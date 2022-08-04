package fi.metatavu.timebank.api.test.functional.data

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Class for utility methods for Dates used in tests
 */
class TestDateUtils {

    companion object {

        /**
         * Gets given date as OffsetDateTime and removes time offset
         *
         * @return String OffsetDateTime without time offset
         */
        fun getODT(dateTime: LocalDateTime): String {
            return "${dateTime}Z"
        }

        /**
         * Gets LocalDate of thirty days ago
         *
         * @return LocalDate
         */
        fun getThirtyDaysAgo(): LocalDate {
            return LocalDate.now().minusDays(30)
        }

        /**
         * Gets first week Dates, starting from thirty days ago
         *
         * @return List of LocalDates
         */
        fun getThirtyDaysAgoFirstWeek(): List<LocalDate> {
            return listOf(
                getThirtyDaysAgo(),
                getThirtyDaysAgo().plusDays(1),
                getThirtyDaysAgo().plusDays(2),
                getThirtyDaysAgo().plusDays(3),
                getThirtyDaysAgo().plusDays(4),
                getThirtyDaysAgo().plusDays(5),
                getThirtyDaysAgo().plusDays(6)
            )
        }

        /**
         * Gets second week Dates, starting from thirty days ago
         *
         * @return List of LocalDates
         */
        fun getThirtyDaysAgoSecondWeek(): List<LocalDate> {
            return getThirtyDaysAgoFirstWeek().map { it.plusWeeks(1) }
        }

        /**
         * Gets third week Dates, starting from thirty days ago
         *
         * @return List of LocalDates
         */
        fun getThirtyDaysAgoThirdWeek(): List<LocalDate> {
            return getThirtyDaysAgoSecondWeek().map { it.plusWeeks(2) }
        }

        /**
         * Gets fourth week Dates, starting from thirty days ago
         *
         * @return List of LocalDates
         */
        fun getThirtyDaysAgoFourthWeek(): List<LocalDate> {
            return getThirtyDaysAgoThirdWeek().map { it.plusWeeks(3) }
        }

        /**
         * Gets LocalDate of 60 days ago
         *
         * @return LocalDate
         */
        fun getSixtyDaysAgo(): LocalDate {
            return LocalDate.now().minusDays(60)
        }

        /**
         * Gets LocalDate of 365 days ago
         *
         * @return LocalDate
         */
        fun getLastYearToday(): LocalDate {
            return LocalDate.now().minusDays(365)
        }
    }
}