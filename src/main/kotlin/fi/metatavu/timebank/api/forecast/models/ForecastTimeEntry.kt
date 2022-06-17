package fi.metatavu.timebank.api.forecast.models

import io.quarkus.runtime.annotations.RegisterForReflection

/**
 * Data class for TimeEntry data coming from Forecast
 */
@RegisterForReflection
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
    val approval_status: String?,
    val created_by: Int,
    val updated_by: Int,
    val created_at: String,
    val updated_at: String,
    val phase: Int?,
    val task_project: String?,
    val invoice_entry: Any?,
    val invoice: Any?
)