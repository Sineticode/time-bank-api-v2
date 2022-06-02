package fi.metatavu.timebank.api.forecast.models

/**
 * Data class for ForecastTimeEntryResponse
 */
data class ForecastTimeEntryResponse(
    val pageContents: List<ForecastTimeEntry>?,
    val pageNumber: Int,
    val pageSize: Int,
    val totalObjectCount: Int,
    val status: Int,
    val message: String?
)