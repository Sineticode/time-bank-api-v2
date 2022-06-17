package fi.metatavu.timebank.api.forecast.models

import io.quarkus.runtime.annotations.RegisterForReflection

/**
 * Data class for ForecastTimeEntryResponse
 */
@RegisterForReflection
data class ForecastTimeEntryResponse(
    val pageContents: List<ForecastTimeEntry>?,
    val pageNumber: Int,
    val pageSize: Int,
    val totalObjectCount: Int,
    val status: Int,
    val message: String?
)