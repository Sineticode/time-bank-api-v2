package fi.metatavu.timebank.api.utils

import fi.metatavu.timebank.api.controllers.PersonsController
import java.time.LocalDate
import java.time.Year
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

/**
 * Vacation Utils class
 */
@ApplicationScoped
class VacationUtils {

    @Inject
    lateinit var personsController: PersonsController

    suspend fun getPersonsVacations(personId: Int): Int {
        val person = personsController.listPersons()?.filter { it.id == personId }?.first()
        val monthsInSeason = LocalDate.now().monthValue - vacationSeasonStart.monthValue
        return if (LocalDate.parse(person?.startDate) < vacationSeasonStart) {
            (monthsInSeason * 2.5).toInt()
        } else {
            (LocalDate.parse(person?.startDate).monthValue * 2.5).toInt()
        }
    }

    companion object {
        const val VACATION_ID = 228255
        val vacationSeasonStart = checkVacationSeasonStart()
        val vacationSeasonEnd = checkVacationSeasonEnd()

        /**
         * Gets the start of current vacation season
         *
         * @return LocalDate
         */
        private fun checkVacationSeasonStart(): LocalDate {
            return if (LocalDate.now().monthValue < 4) {
                LocalDate.of(Year.now().value - 1, 4, 1)
            } else {
                LocalDate.of(Year.now().value, 4, 1)
            }
        }

        /**
         * Gets the end of current vacation season
         *
         * @return LocalDate
         */
        private fun checkVacationSeasonEnd(): LocalDate {
            return if (LocalDate.now().monthValue < 4) {
                LocalDate.of(Year.now().value, 3, 31)
            } else {
                LocalDate.of(Year.now().value + 1, 3, 31)
            }
        }
    }
}