package fi.metatavu.timebank.api.forecast.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.quarkus.runtime.annotations.RegisterForReflection

/**
 * Data class for ForecastTimeEntryResponse
 */
@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
class ForecastTimeEntryResponse {
    val pageContents: List<ForecastTimeEntry>? = null
    val pageSize: Int = 0
    val totalObjectCount: Int = 0
}