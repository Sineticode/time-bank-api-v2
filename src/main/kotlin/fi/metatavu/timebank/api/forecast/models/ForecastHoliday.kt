package fi.metatavu.timebank.api.forecast.models

/**
 * Data class for holiday data coming from Forecast
 */
data class ForecastHoliday(
    val id: Int,
    val holiday_calendar_id: Int,
    val year: Int,
    val month: Int,
    val day: Int,
    val name: String,
    val created_by: Int,
    val updated_by: Int,
    val created_at: String,
    val updated_at: String
)