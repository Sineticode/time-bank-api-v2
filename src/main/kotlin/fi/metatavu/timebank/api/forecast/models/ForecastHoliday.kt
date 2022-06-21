package fi.metatavu.timebank.api.forecast.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

/**
 * Data class for holiday data coming from Forecast
 */
@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
class ForecastHoliday {
    val id: Int = 0
    @JsonProperty("holiday_calendar_id")
    val holidayCalendarId: Int = 0
    val year: Int = 0
    val month: Int = 0
    val day: Int = 0
    val name: String = ""
}