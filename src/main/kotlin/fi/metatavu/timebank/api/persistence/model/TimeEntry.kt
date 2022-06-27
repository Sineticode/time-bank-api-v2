package fi.metatavu.timebank.api.persistence.model

import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import kotlin.reflect.full.memberProperties

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
        val baseValue = this
        val differentFields = TimeEntry::class.memberProperties.filter {
            val firstValue = it.get(baseValue)
            val secondValue = it.get(timeEntry)
            !Objects.equals(firstValue, secondValue)
        }

        return differentFields.size == 1
    }
}