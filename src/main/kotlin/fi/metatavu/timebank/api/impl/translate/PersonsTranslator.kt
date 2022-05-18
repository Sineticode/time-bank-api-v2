package fi.metatavu.timebank.api.impl.translate

import fi.metatavu.timebank.api.persistence.model.Person
import java.time.OffsetDateTime
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class PersonsTranslator {

    suspend fun translatePersons(persons: Array<ForecastPerson>): List<Person> {
        val filteredPersons: MutableList<Person> = mutableListOf()
        persons.map{ i ->
            val translatedPerson = Person()
            translatedPerson.personId = i.id?.toLong()
            translatedPerson.firstName = i.first_name
            translatedPerson.lastName = i.last_name
            translatedPerson.email = i.email
            translatedPerson.userType = i.user_type
            translatedPerson.monday = i.monday
            translatedPerson.tuesday = i.tuesday
            translatedPerson.wednesday = i.wednesday
            translatedPerson.thursday = i.thursday
            translatedPerson.friday = i.friday
            translatedPerson.saturday = i.saturday
            translatedPerson.sunday = i.sunday
            translatedPerson.active = i.active
            translatedPerson.defaultRole = i.default_role
            translatedPerson.cost = i.cost
            translatedPerson.clientId = i.client_id
            translatedPerson.holidayCalendarId = i.holiday_calendar_id
            translatedPerson.language = i.language
            translatedPerson.startDate = i.start_date
            translatedPerson.endDate = i.end_date
            translatedPerson.createdBy = i.created_by
            translatedPerson.updatedBy = i.updated_by
            translatedPerson.createdAt = OffsetDateTime.parse(i.created_at)
            translatedPerson.updatedAt = OffsetDateTime.parse(i.updated_at)
            filteredPersons.add(translatedPerson)
        }

        return filteredPersons
    }

}

data class ForecastPerson(
    val id: Int?,
    val first_name: String?,
    val last_name: String?,
    val email: String?,
    val user_type: String?,
    val monday: Int?,
    val tuesday: Int?,
    val wednesday: Int?,
    val thursday: Int?,
    val friday: Int?,
    val saturday: Int?,
    val sunday: Int?,
    val active: Boolean?,
    val default_role: Int?,
    val department_id: Int?,
    val cost: Double?,
    val language: String?,
    val created_by: Int?,
    val updated_by: Int?,
    val client_id: Int?,
    val holiday_calendar_id: Int?,
    val start_date: String?,
    val end_date: String?,
    val created_at: String?,
    val updated_at: String?
)