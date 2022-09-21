package fi.metatavu.timebank.api.impl.translate

import fi.metatavu.timebank.api.forecast.models.ForecastPerson
import fi.metatavu.timebank.model.Person
import javax.enterprise.context.ApplicationScoped

/**
 * Translates ForecastPerson object to Person object
 */
@ApplicationScoped
class PersonsTranslator: AbstractTranslator<ForecastPerson, Person>() {

    override fun translate(entity: ForecastPerson): Person {
        return Person(
            id = entity.id,
            firstName = entity.firstName,
            lastName = entity.lastName,
            email = entity.email,
            monday = entity.monday,
            tuesday = entity.tuesday,
            wednesday = entity.wednesday,
            thursday = entity.thursday,
            friday = entity.friday,
            saturday = entity.saturday,
            sunday = entity.sunday,
            active = entity.active,
            startDate = entity.startDate,
            language = entity.language,
            unspentVacations = entity.unspentVacations,
            spentVacations = entity.spentVacations,
            minimumBillableRate = entity.minimumBillableRate
        )
    }

    override fun translate(entities: List<ForecastPerson>): List<Person> {
        return entities.map(this::translate)
    }
}