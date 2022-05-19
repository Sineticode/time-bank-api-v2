package fi.metatavu.timebank.api.impl.translate

import fi.metatavu.timebank.api.forecast.models.ForecastPerson
import fi.metatavu.timebank.api.persistence.model.Person
import java.time.OffsetDateTime
import javax.enterprise.context.ApplicationScoped

/**
 * Translates ForecastPerson object to Person object
 */
@ApplicationScoped
class PersonsTranslator: AbstractTranslator<ForecastPerson, Person>() {

    override fun translate(entity: ForecastPerson): Person {
        val translatedPerson = Person()
        translatedPerson.personId = entity.id.toLong()
        translatedPerson.firstName = entity.first_name
        translatedPerson.lastName = entity.last_name
        translatedPerson.email = entity.email
        translatedPerson.monday = entity.monday
        translatedPerson.tuesday = entity.tuesday
        translatedPerson.wednesday = entity.wednesday
        translatedPerson.thursday = entity.thursday
        translatedPerson.friday = entity.friday
        translatedPerson.saturday = entity.saturday
        translatedPerson.sunday = entity.sunday
        translatedPerson.active = entity.active
        translatedPerson.defaultRole = entity.default_role
        translatedPerson.cost = entity.cost
        translatedPerson.holidayCalendarId = entity.holiday_calendar_id
        translatedPerson.language = entity.language
        translatedPerson.startDate = entity.start_date
        translatedPerson.endDate = entity.end_date
        translatedPerson.createdBy = entity.created_by
        translatedPerson.updatedBy = entity.updated_by
        translatedPerson.createdAt = OffsetDateTime.parse(entity.created_at)
        translatedPerson.updatedAt = OffsetDateTime.parse(entity.updated_at)
        return translatedPerson
    }

    override fun translate(entities: List<ForecastPerson>): List<Person> {
        return entities.map(this::translate)
    }
}