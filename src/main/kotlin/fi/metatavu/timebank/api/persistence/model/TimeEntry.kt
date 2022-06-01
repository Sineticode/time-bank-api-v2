package fi.metatavu.timebank.api.persistence.model

import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.PrePersist
import javax.persistence.Table

/**
 * TimeEntry JPA entity
 */
@Entity
@Table(name = "TimeEntry")
class TimeEntry {

    @Id
    @Column(name = "entryId")
    lateinit var entryId: UUID

    @Column(name = "person")
    var person: Int? = null
    
    @Column(name = "internalTime")
    var internalTime: Int? = null
    
    @Column(name = "projectTime")
    var projectTime: Int? = null

    @Column(name = "date")
    var date: LocalDate? = null

    @Column(name = "createdAt")
    var createdAt: OffsetDateTime? = null

    @Column(name = "updatedAt")
    var updatedAt: OffsetDateTime? = null
}