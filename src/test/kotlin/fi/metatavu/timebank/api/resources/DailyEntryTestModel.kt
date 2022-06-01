package fi.metatavu.timebank.api.resources

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Person's daily entry for testing
 *
 * @param person Person ID in Forecast
 * @param internalTime Amount of internal time per time entry
 * @param projectTime Amount of project time per time entry
 * @param logged Amount of time logged per time entry
 * @param expected Amount of time expected per time entry
 * @param balance Total amount of worktime per time entry
 * @param date Date of time entry
 */
data class DailyEntryTestModel (

    /* Person ID in Forecast */
    @JsonProperty("person")
    val person: Int,

    /* Amount of internal time per time entry */
    @JsonProperty("internalTime")
    val internalTime: Int,

    /* Amount of project time per time entry */
    @JsonProperty("projectTime")
    val projectTime: Int,

    /* Amount of time logged per time entry */
    @JsonProperty("logged")
    val logged: Int,

    /* Amount of time expected per time entry */
    @JsonProperty("expected")
    val expected: Int,

    /* Total amount of worktime per time entry */
    @JsonProperty("balance")
    val balance: Int,

    /* Date of time entry */
    @JsonProperty("date")
    val date: String
)