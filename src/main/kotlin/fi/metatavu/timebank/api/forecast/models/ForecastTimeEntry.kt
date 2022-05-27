package fi.metatavu.timebank.api.forecast.models

/**
 * Data class for TimeEntry data coming from Forecast
 */
data class ForecastTimeEntry(
    val id: Int,
    val person: Int,
    val project: Int?,
    val card: Int?,
    val task: Int?,
    val non_project_time: Int?,
    val time_registered: Int,
    val date: String,
    val notes: String?,
    val approval_status: String,
    val created_by: Int,
    val updated_by: Int,
    val created_at: String,
    val updated_at: String
)