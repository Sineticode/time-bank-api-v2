package fi.metatavu.timebank.api.persistence.model

import java.time.OffsetDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.validation.constraints.NotEmpty

@Entity
class Person {

    @NotEmpty
    @Id
    @Column(unique = true, nullable = false)
    var personId: Long? = 0

    @NotEmpty
    @Column(nullable = false)
    var firstName: String = ""

    @NotEmpty
    @Column(nullable = false)
    var lastName: String = ""

    @NotEmpty
    @Column(nullable = false)
    var email: String = ""

    @NotEmpty
    @Column(nullable = false)
    var userType: String = ""

    @NotEmpty
    @Column(nullable = false)
    var clientId: Int = 0

    @NotEmpty
    @Column(nullable = false)
    var holidayCalendarId: Int = 0

    @NotEmpty
    @Column(nullable = false)
    var monday: Int = 0

    @NotEmpty
    @Column(nullable = false)
    var tuesday: Int = 0

    @NotEmpty
    @Column(nullable = false)
    var wednesday: Int = 0

    @NotEmpty
    @Column(nullable = false)
    var thursday: Int = 0

    @NotEmpty
    @Column(nullable = false)
    var friday: Int = 0

    @NotEmpty
    @Column(nullable = false)
    var saturday: Int = 0

    @NotEmpty
    @Column(nullable = false)
    var sunday: Int = 0

    @NotEmpty
    @Column(nullable = false)
    var active: Boolean = false

    @NotEmpty
    @Column(nullable = false)
    var defaultRole: Int = 0

    @NotEmpty
    @Column(nullable = false)
    var cost: Double = 0.0

    @NotEmpty
    @Column(nullable = false)
    var language: String = ""

    @NotEmpty
    @Column(nullable = false)
    var starDate: String = ""

    @NotEmpty
    @Column(nullable = true)
    var endDate: String? = ""

    @NotEmpty
    @Column(nullable = false)
    var createdBy: Int = 0

    @NotEmpty
    @Column(nullable = false)
    var updatedBy: Int = 0

    @NotEmpty
    @Column(nullable = false)
    var createdAt: OffsetDateTime = OffsetDateTime.now()

    @NotEmpty
    @Column(nullable = false)
    var updatedAt: OffsetDateTime = OffsetDateTime.now()
}