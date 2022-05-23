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
            entity.id,
            entity.first_name,
            entity.last_name?: "",
            entity.monday,
            entity.tuesday,
            entity.wednesday,
            entity.thursday,
            entity.friday,
            entity.saturday,
            entity.sunday,
            entity.active,
        entity.start_date?: "",
        entity.default_role?: 0
        )
    }

    override fun translate(entities: List<ForecastPerson>): List<fi.metatavu.timebank.model.Person> {
        return entities.map(this::translate)
    }
}