package fi.metatavu.timebank.api.scheduler

import fi.metatavu.timebank.api.controllers.SynchronizeController
import io.quarkus.scheduler.Scheduled
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import java.time.LocalDate
import javax.inject.Inject

class ScheduledSynchronization {

    @Inject
    lateinit var synchronizeController: SynchronizeController

    @Inject
    lateinit var logger: Logger

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