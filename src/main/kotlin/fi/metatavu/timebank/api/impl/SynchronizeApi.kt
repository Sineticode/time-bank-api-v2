package fi.metatavu.timebank.api.impl

import fi.metatavu.timebank.api.controllers.SynchronizeController
import fi.metatavu.timebank.spec.SynchronizeApi
import io.quarkus.scheduler.Scheduled
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import java.time.LocalDate
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.ws.rs.core.Response

/**
 * API implementation for Synchronize API
 */
@RequestScoped
class SynchronizeApi:  SynchronizeApi, AbstractApi() {

    @Inject
    lateinit var synchronizeController: SynchronizeController

    @Inject
    lateinit var logger: Logger

    override suspend fun synchronizeTimeEntries(before: LocalDate?, after: LocalDate?): Response {
        var synchronizedEntries = 0
//        Comment two lines below to synchronize without Keycloak usage
        if (after == null && !isAdmin()) return createUnauthorized("Forbidden to synchronize all time!")
        else if (isAdmin() || (after != null && after > LocalDate.now().minusDays(1)))synchronizedEntries = synchronizeController.synchronize(after)
        if (synchronizedEntries == 0) return createBadRequest("Nothing to synchronize!")
//         Un-comment line below to synchronize without Keycloak usage
//         synchronizedEntries = synchronizeController.synchronize(after)
        return createOk("Synchronized $synchronizedEntries entries from Forecast!")
    }

    @Scheduled(cron = "0 15 3 * * ?")
    fun scheduledSynchronization(){
        runBlocking {
            val afterDate = LocalDate.now().minusDays(1)
            logger.info("[${LocalDate.now()}] Scheduled synchronization starting with...")
            val synchronizedEntries = synchronizeController.synchronize(afterDate)
            logger.info("Synchronized $synchronizedEntries entries from Forecast!")
        }
    }

}