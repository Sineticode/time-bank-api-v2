package fi.metatavu.timebank.api.forecast.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

/**
 * Data class for TimeEntry data coming from Forecast
 */
@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
class ForecastTimeEntry {
    val id: Int = 0
    val person: Int = 0
    @JsonProperty("non_project_time")
    val nonProjectTime: Int? = null
    @JsonProperty("time_registered")
    val timeRegistered: Int = 0
    val date: String = ""
    @JsonProperty("created_by")
    val createdBy: Int = 0
    @JsonProperty("updated_by")
    val updatedBy: Int = 0
    @JsonProperty("created_at")
    val createdAt: String = ""
    @JsonProperty("updated_at")
    val updatedAt: String = ""
}