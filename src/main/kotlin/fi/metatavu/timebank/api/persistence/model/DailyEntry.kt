package fi.metatavu.timebank.api.persistence.model

import java.time.OffsetDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.PrePersist
import javax.persistence.Table

/**
 * DailyEntry JPA entity
 */
@Entity
@Table(name = "DailyEntry")
class DailyEntry {

    @Id
    @Column(name = "entryId")
    lateinit var entryId: String

    @Column(name = "person")
    var person: Int? = null
    
    @Column(name = "internalTime")
    var internalTime: Int? = null
    
    @Column(name = "projectTime")
    var projectTime: Int? = null

    @Column(name = "logged")
    var logged: Int? = null

    @Column(name = "expected")
    var expected: Int? = null

    @Column(name = "total")
    var total: Int? = null

    @Column(name = "date")
    var date: OffsetDateTime = OffsetDateTime.now()

    @PrePersist
    fun onCreate() {
        entryId = UUID.randomUUID().toString()
    }
}