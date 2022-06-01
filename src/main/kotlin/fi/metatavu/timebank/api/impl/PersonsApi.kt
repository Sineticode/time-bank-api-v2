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
        if (loggedUserId == null) return createUnauthorized("Invalid token!")
        val entries = personsController.makePersonTotal(personId, timespan ?: Timespan.ALL_TIME)
            ?: return createNotFound("Cannot calculate totals for given person")
        return createOk(entries)
    }

    override suspend fun listPersons(active: Boolean?): Response {
        if (loggedUserId == null) return createUnauthorized("Invalid token!")
        val persons = personsController.listPersons(active)
            ?: return createNotFound("No persons found!")
        val translatedPersons = personsTranslator.translate(persons)
        return createOk(translatedPersons)
    }
}