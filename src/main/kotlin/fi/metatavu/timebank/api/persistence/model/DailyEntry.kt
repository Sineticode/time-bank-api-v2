package fi.metatavu.timebank.api.persistence.model

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import net.bytebuddy.implementation.bind.annotation.Empty
import java.time.OffsetDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.validation.constraints.NotEmpty

@Entity
class DailyEntry: PanacheEntity() {

    @NotEmpty
    @Column(unique = true, nullable = false)
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