package fi.metatavu.timebank.api.persistence.model

import java.time.LocalDate
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class DailyEntry {

    @Id
    @Column(unique = true, nullable = false)
    var entryId: UUID = UUID.randomUUID()

    @Column(nullable = false)
    var person: Int = 0

    @Column(nullable = false)
    var internalTime: Int = 0

    @Column(nullable = false)
    var projectTime: Int = 0

    @Column(nullable = false)
    var logged: Int = 0

    @Column(nullable = false)
    var expected: Int = 0

    @Column(nullable = false)
    var total: Int = 0

    @Column(nullable = false)
    var date: LocalDate = LocalDate.now()
}