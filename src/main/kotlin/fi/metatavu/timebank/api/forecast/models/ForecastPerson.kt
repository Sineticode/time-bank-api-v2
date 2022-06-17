package fi.metatavu.timebank.api.forecast.models

import io.quarkus.runtime.annotations.RegisterForReflection

/**
 * Data class for Person data coming from Forecast
 */
@RegisterForReflection
data class ForecastPerson(
    val id: Int,
    val first_name: String,
    val last_name: String?,
    val email: String?,
    val monday: Int,
    val tuesday: Int,
    val wednesday: Int,
    val thursday: Int,
    val friday: Int,
    val saturday: Int,
    val sunday: Int,
    val active: Boolean,
    val default_role: Int?,
    val cost: Double,
    val language: String,
    val created_by: Int?,
    val updated_by: Int?,
    val client_id: Int?,
    val holiday_calendar_id: Int,
    val start_date: String?,
    val end_date: String?,
    val created_at: String?,
    val updated_at: String?,
    val department_id: Any?,
    val permissions: List<String>,
    val is_system_user: Boolean
)