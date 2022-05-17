package fi.metatavu.timebank.api.impl

import fi.metatavu.timebank.api.controllers.PersonsController
import fi.metatavu.timebank.model.Timespan
import fi.metatavu.timebank.spec.PersonsApi
import javax.inject.Inject
import javax.ws.rs.core.Response

class PersonsApi: PersonsApi, AbstractApi() {

    @Inject
    lateinit var personsController: PersonsController

    override suspend fun listPersonTotalTime(personId: Int, timespan: Timespan?): Response {
    return Response.ok(personsController.getPersonTotal(personId)).build()
    }

    override suspend fun listPersons(active: Boolean?): Response {
        return Response.ok(personsController.getPersonsFromForecast(active)).build()
    }
}