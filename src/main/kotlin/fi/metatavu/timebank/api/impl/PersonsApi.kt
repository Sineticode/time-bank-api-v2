package fi.metatavu.timebank.api.impl

import fi.metatavu.timebank.api.controllers.PersonsController
import fi.metatavu.timebank.api.impl.translate.PersonsTranslator
import fi.metatavu.timebank.model.Timespan
import fi.metatavu.timebank.spec.PersonsApi
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.ws.rs.core.Response

/**
 * API implementation for Persons API
 */
@RequestScoped
class PersonsApi: PersonsApi, AbstractApi() {

    @Inject
    lateinit var personsController: PersonsController

    @Inject
    lateinit var personsTranslator: PersonsTranslator

    override suspend fun listPersonTotalTime(personId: Int, timespan: Timespan?): Response {
        val entries = personsController.makePersonTotal(personId, timespan ?: Timespan.ALL_TIME)
        if (entries.isEmpty()) createNotFound("No daily entries found for given person!")
        return createOk(entries)
    }

    override suspend fun listPersons(active: Boolean?): Response {
        val persons = personsTranslator.translate(personsController.listPersons(active))
        if (persons.isEmpty()) createNotFound("No persons found!")
        return createOk(persons)
    }
}