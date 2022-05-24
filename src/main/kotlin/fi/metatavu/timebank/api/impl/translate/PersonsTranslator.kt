package fi.metatavu.timebank.api.impl.translate

import fi.metatavu.timebank.api.forecast.models.ForecastPerson
import javax.enterprise.context.ApplicationScoped

/**
 * Translates ForecastPerson object to Person object
 */
@ApplicationScoped
class PersonsTranslator: AbstractTranslator<ForecastPerson, fi.metatavu.timebank.model.Person>() {

    override fun translate(entity: ForecastPerson): fi.metatavu.timebank.model.Person {
        return fi.metatavu.timebank.model.Person(
            id = entity.id,
            firstName = entity.first_name,
            lastName = entity.last_name ?: "",
            monday = entity.monday,
            tuesday = entity.tuesday,
            wednesday = entity.wednesday,
            thursday = entity.thursday,
            friday = entity.friday,
            saturday = entity.saturday,
            sunday = entity.sunday,
            active = entity.active,
            startDate = entity.start_date ?: "",
            defaultRole = entity.default_role ?: 0
        )
    }

    override fun translate(entities: List<ForecastPerson>): List<fi.metatavu.timebank.model.Person> {
        return entities.map(this::translate)
    }
}