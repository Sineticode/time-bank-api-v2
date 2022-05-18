package fi.metatavu.timebank.api.controllers

import fi.metatavu.timebank.api.persistence.model.Person
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class SynchronizeController {

    @Inject
    lateinit var personsController: PersonsController
    suspend fun synchronize() {
        var persons: List<Person>? = personsController.getPersonsFromForecast(active = true)
        persons = persons?.filter { i -> i.personId != null }
        persons?.forEach{ i -> println(i.firstName)}
    }

}