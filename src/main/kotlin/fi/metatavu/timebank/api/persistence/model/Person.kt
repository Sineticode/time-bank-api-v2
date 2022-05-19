package fi.metatavu.timebank.api.persistence.model

import java.time.OffsetDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.validation.constraints.NotEmpty

/**
 * Person JPA entity
 */
@Entity
class Person {

    @NotEmpty
    @Id
    @Column(unique = true)
    var personId: Long? = 0

    @NotEmpty
    @Column
    var firstName: String? = ""

    @NotEmpty
    @Column
    var lastName: String? = ""

    @NotEmpty
    @Column
    var email: String? = ""

    @NotEmpty
    @Column
    var holidayCalendarId: Int? = 0

    @NotEmpty
    @Column
    var monday: Int? = 0

    @NotEmpty
    @Column
    var tuesday: Int? = 0

    @NotEmpty
    @Column
    var wednesday: Int? = 0

    @NotEmpty
    @Column
    var thursday: Int? = 0

    @NotEmpty
    @Column
    var friday: Int? = 0

    @NotEmpty
    @Column
    var saturday: Int? = 0

    @NotEmpty
    @Column
    var sunday: Int? = 0

    @NotEmpty
    @Column
    var active: Boolean? = false

    @NotEmpty
    @Column
    var defaultRole: Int? = 0

    @NotEmpty
    @Column
    var cost: Double? = 0.0

    @NotEmpty
    @Column
    var language: String? = ""

    @NotEmpty
    @Column
    var startDate: String? = ""

    @NotEmpty
    @Column
    var endDate: String? = ""

    @NotEmpty
    @Column
    var createdBy: Int? = 0

    @NotEmpty
    @Column
    var updatedBy: Int? = 0

    @NotEmpty
    @Column
    var createdAt: OffsetDateTime? = OffsetDateTime.now()

    @NotEmpty
    @Column
    var updatedAt: OffsetDateTime? = OffsetDateTime.now()
}