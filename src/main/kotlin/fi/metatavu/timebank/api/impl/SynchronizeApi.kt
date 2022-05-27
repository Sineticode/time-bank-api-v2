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
        val synchronizedEntries = synchronizeController.synchronize(after)
        if (synchronizedEntries == 0) return createBadRequest("Nothing to synchronize!")
        if (synchronizedEntries == -1) return createUnauthorized("Forbidden to synchronize all time!")
        return createOk("Synchronized $synchronizedEntries entries from Forecast!")
    }

    @Scheduled(cron = "0 36 13 * * ?")
    fun scheduledSynchronization(){
        runBlocking {
            val afterDate = LocalDate.now().minusDays(1)
            logger.info("[${LocalDate.now()}] Scheduled synchronization starting with...")
            val synchronizedEntries = synchronizeController.synchronize(afterDate)
            logger.info("Synchronized $synchronizedEntries entries from Forecast!")
        }
    }

}