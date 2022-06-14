package fi.metatavu.timebank.api.persistence.model

import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

/**
 * TimeEntry JPA entity
 */
@Entity
class TimeEntry {

    @Id
    @Column
    lateinit var entryId: UUID

    @Column
    var forecastId: Int? = null

    @Column
    var person: Int? = null

    @Column
    var internalTime: Int? = null

    @Column
    var projectTime: Int? = null

    @Column
    var date: LocalDate? = null

    @Column
    var createdAt: OffsetDateTime? = null

    @Column
    var updatedAt: OffsetDateTime? = null

    /**
     * Compares object equality ignoring entryId
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        other as TimeEntry
        return forecastId == other.forecastId &&
            person == other.person &&
            internalTime == other.internalTime &&
            projectTime == other.projectTime &&
            date == other.date &&
            createdAt == other.createdAt &&
            updatedAt == other.updatedAt
    }
}