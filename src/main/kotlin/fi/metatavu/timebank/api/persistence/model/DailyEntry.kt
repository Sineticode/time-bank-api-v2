package fi.metatavu.timebank.api.persistence.model

import java.time.OffsetDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.validation.constraints.NotEmpty

/**
 * DailyEntry JPA entity
 */
@Entity
class DailyEntry {

    @Id
    @Column(nullable = false)
    var entryId: UUID = UUID.randomUUID()

    @NotEmpty
    @Column(nullable = false)
    var person: Int = 0

    @NotEmpty
    @Column(nullable = false)
    var internalTime: Int = 0

    @NotEmpty
    @Column(nullable = false)
    var projectTime: Int = 0

    @NotEmpty
    @Column(nullable = false)
    var logged: Int = 0

    @NotEmpty
    @Column(nullable = false)
    var expected: Int = 0

    @NotEmpty
    @Column(nullable = false)
    var total: Int = 0

    @NotEmpty
    @Column(nullable = false)
    var date: OffsetDateTime = OffsetDateTime.now()
}