package fi.metatavu.timebank.api.impl

import fi.metatavu.timebank.api.controllers.SynchronizeController
import fi.metatavu.timebank.spec.SynchronizeApi
import java.time.LocalDate
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.ws.rs.core.Response

@RequestScoped
class SynchronizeApi:  SynchronizeApi, AbstractApi() {

    @Inject
    lateinit var synchronizeController: SynchronizeController

    override suspend fun synchronizeTimeEntries(before: LocalDate?, after: LocalDate?): Response {
        val entries = synchronizeController.synchronize(after)
        if (entries == 0) return createBadRequest("Nothing to synchronize!")
        return createOk("Synchronized $entries entries from Forecast!")
    }
}
