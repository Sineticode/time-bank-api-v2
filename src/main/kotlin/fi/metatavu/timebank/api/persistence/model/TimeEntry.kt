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
    fun areTwoObjectsSame(timeEntry: TimeEntry): Boolean {
        if (this === timeEntry) return true
        return forecastId == timeEntry.forecastId &&
                person == timeEntry.person &&
                internalTime == timeEntry.internalTime &&
                projectTime == timeEntry.projectTime &&
                date == timeEntry.date &&
                createdAt == timeEntry.createdAt &&
                updatedAt == timeEntry.updatedAt
    }
}