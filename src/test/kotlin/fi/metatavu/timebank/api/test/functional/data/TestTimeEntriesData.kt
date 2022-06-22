package fi.metatavu.timebank.api.test.functional.data

import fi.metatavu.timebank.api.forecast.models.ForecastTimeEntry

/**
 * Class for test time entries mock data
 */
class TestTimeEntriesData {

    companion object {

        fun getForecastTimeEntries(): List<ForecastTimeEntry> {
            val timeEntryA = ForecastTimeEntry()
            timeEntryA.id = 0
            timeEntryA.person = 2
            timeEntryA.nonProjectTime = 123
            timeEntryA.timeRegistered = 435
            timeEntryA.date = "2021-07-31"
            timeEntryA.createdBy = 2
            timeEntryA.updatedBy = 2
            timeEntryA.createdAt = "2021-07-31T12:00:00+03:00"
            timeEntryA.updatedAt = "2021-07-31T12:00:00+03:00"
            val timeEntryB = ForecastTimeEntry()
            timeEntryB.id = 1
            timeEntryB.person = 1
            timeEntryB.nonProjectTime = 255455
            timeEntryB.timeRegistered = 100
            timeEntryB.date = "2022-06-05"
            timeEntryB.createdBy = 1
            timeEntryB.updatedBy = 1
            timeEntryB.createdAt = "2022-05-30T04:53:33+02:00"
            timeEntryB.updatedAt = "2022-05-30T04:53:33+02:00"
            val timeEntryC = ForecastTimeEntry()
            timeEntryC.id = 2
            timeEntryC.person = 1
            timeEntryC.nonProjectTime = null
            timeEntryC.timeRegistered = 100
            timeEntryC.date = "2022-05-30"
            timeEntryC.createdBy = 1
            timeEntryC.updatedBy = 1
            timeEntryC.createdAt = "2022-05-30T04:53:33+02:00"
            timeEntryC.updatedAt = "2022-05-30T04:53:33+02:00"
            val timeEntryD = ForecastTimeEntry()
            timeEntryD.id = 3
            timeEntryD.person = 2
            timeEntryD.nonProjectTime = null
            timeEntryD.timeRegistered = 400
            timeEntryD.date = "2022-05-12"
            timeEntryD.createdBy = 1
            timeEntryD.updatedBy = 1
            timeEntryD.createdAt = "2022-05-30T04:53:33+02:00"
            timeEntryD.updatedAt = "2022-05-30T04:53:33+02:00"
            val timeEntryE = ForecastTimeEntry()
            timeEntryE.id = 4
            timeEntryE.person = 2
            timeEntryE.nonProjectTime = null
            timeEntryE.timeRegistered = 400
            timeEntryE.date = "2022-04-30"
            timeEntryE.createdBy = 1
            timeEntryE.updatedBy = 1
            timeEntryE.createdAt = "2022-05-30T04:53:33+02:00"
            timeEntryE.updatedAt = "2022-05-30T04:53:33+02:00"
            val timeEntryF = ForecastTimeEntry()
            timeEntryF.id = 5
            timeEntryF.person = 3
            timeEntryF.nonProjectTime = null
            timeEntryF.timeRegistered = 122
            timeEntryF.date = "2022-04-02"
            timeEntryF.createdBy = 1
            timeEntryF.updatedBy = 1
            timeEntryF.createdAt = "2022-05-30T04:53:33+02:00"
            timeEntryF.updatedAt = "2022-05-30T04:53:33+02:00"
            val timeEntryG = ForecastTimeEntry()
            timeEntryG.id = 6
            timeEntryG.person = 3
            timeEntryG.nonProjectTime = 255455
            timeEntryG.timeRegistered = 372
            timeEntryG.date = "2022-03-28"
            timeEntryG.createdBy = 1
            timeEntryG.updatedBy = 1
            timeEntryG.createdAt = "2022-05-30T04:53:33+02:00"
            timeEntryG.updatedAt = "2022-05-30T04:53:33+02:00"
            val timeEntryH = ForecastTimeEntry()
            timeEntryH.id = 7
            timeEntryH.person = 3
            timeEntryH.nonProjectTime = 114753
            timeEntryH.timeRegistered = 378
            timeEntryH.date = "2022-03-16"
            timeEntryH.createdBy = 1
            timeEntryH.updatedBy = 1
            timeEntryH.createdAt = "2022-05-30T04:53:33+02:00"
            timeEntryH.updatedAt = "2022-05-30T04:53:33+02:00"
            val timeEntryI = ForecastTimeEntry()
            timeEntryI.id = 8
            timeEntryI.person = 3
            timeEntryI.nonProjectTime = null
            timeEntryI.timeRegistered = 122
            timeEntryI.date = "2022-03-14"
            timeEntryI.createdBy = 1
            timeEntryI.updatedBy = 1
            timeEntryI.createdAt = "2022-05-30T04:53:33+02:00"
            timeEntryI.updatedAt = "2022-05-30T04:53:33+02:00"
            val timeEntryJ = ForecastTimeEntry()
            timeEntryJ.id = 9
            timeEntryJ.person = 3
            timeEntryJ.nonProjectTime = null
            timeEntryJ.timeRegistered = 52
            timeEntryJ.date = "2022-01-03"
            timeEntryJ.createdBy = 1
            timeEntryJ.updatedBy = 1
            timeEntryJ.createdAt = "2022-05-30T04:53:33+02:00"
            timeEntryJ.updatedAt = "2022-05-30T04:53:33+02:00"
            val timeEntryK = ForecastTimeEntry()
            timeEntryK.id = 10
            timeEntryK.person = 1
            timeEntryK.nonProjectTime = 12
            timeEntryK.timeRegistered = 120
            timeEntryK.date = "2022-02-28"
            timeEntryK.createdBy = 1
            timeEntryK.updatedBy = 1
            timeEntryK.createdAt = "2022-02-28T04:53:33+02:00"
            timeEntryK.updatedAt = "2022-03-01T04:53:33+02:00"
            val timeEntryL = ForecastTimeEntry()
            timeEntryL.id = 11
            timeEntryL.person = 2
            timeEntryL.nonProjectTime = null
            timeEntryL.timeRegistered = 400
            timeEntryL.date = "2022-06-07"
            timeEntryL.createdBy = 1
            timeEntryL.updatedBy = 1
            timeEntryL.createdAt = "2022-06-07T04:53:33+02:00"
            timeEntryL.updatedAt = "2022-06-07T04:53:33+02:00"
            val timeEntryM = ForecastTimeEntry()
            timeEntryM.id = 12
            timeEntryM.person = 1
            timeEntryM.nonProjectTime = null
            timeEntryM.timeRegistered = 312
            timeEntryM.date = "2022-05-29"
            timeEntryM.createdBy = 1
            timeEntryM.updatedBy = 1
            timeEntryM.createdAt = "2022-05-29T04:53:33+02:00"
            timeEntryM.updatedAt = "2022-05-29T04:53:33+02:00"
            val timeEntryN = ForecastTimeEntry()
            timeEntryN.id = 13
            timeEntryN.person = 2
            timeEntryN.nonProjectTime = 1
            timeEntryN.timeRegistered = 213
            timeEntryN.date = "2022-06-10"
            timeEntryN.createdBy = 1
            timeEntryN.updatedBy = 1
            timeEntryN.createdAt = "2022-06-10T04:53:33+02:00"
            timeEntryN.updatedAt = "2022-06-10T04:53:33+02:00"
            val timeEntryP = ForecastTimeEntry()
            timeEntryP.id = 14
            timeEntryP.person = 5
            timeEntryP.nonProjectTime = null
            timeEntryP.timeRegistered = 435
            timeEntryP.date = "2022-06-30"
            timeEntryP.createdBy = 5
            timeEntryP.updatedBy = 5
            timeEntryP.createdAt = "2022-06-30T12:00:00+02:00"
            timeEntryP.updatedAt = "2022-06-30T12:00:00+02:00"
            return listOf(timeEntryA, timeEntryB, timeEntryC, timeEntryD, timeEntryE,
                timeEntryF, timeEntryG, timeEntryH, timeEntryI, timeEntryJ, 
                timeEntryK, timeEntryL, timeEntryM, timeEntryN, timeEntryP)
        }

        fun getUpdatedForecastTimeEntry(): List<ForecastTimeEntry> {
            val updatedTimeEntry = ForecastTimeEntry()
            updatedTimeEntry.id = 14
            updatedTimeEntry.person = 5
            updatedTimeEntry.nonProjectTime = 789
            updatedTimeEntry.timeRegistered = 435
            updatedTimeEntry.date = "2022-06-30"
            updatedTimeEntry.createdBy = 1
            updatedTimeEntry.updatedBy = 1
            updatedTimeEntry.createdAt = "2022-06-30T12:00:00+02:00"
            updatedTimeEntry.updatedAt = "2022-06-30T16:07:33+02:00"
            return listOf(updatedTimeEntry)
        }

        fun generateRandomForecastTimeEntries(pageNumber: Int): List<ForecastTimeEntry> {
            val generatedEntries = mutableListOf<ForecastTimeEntry>()
            val amountToGenerate = if (pageNumber == 1) 1000 else 200
            var id = pageNumber * 1000
            while (generatedEntries.size < amountToGenerate) {
                val year = (2021..2022).random()
                val month = (1..12).random()
                val day =
                    if (month == 2) (1..28).random()
                    else if (month % 2 != 0 && month < 8) (1..31).random()
                    else if (month % 2 == 0 && month >= 8
                    ) (1..31).random()
                    else (1..30).random()
                val monthString = if (month < 10) "0$month" else "$month"
                val dayString = if (day < 10) "0$day" else "$day"
                val date = "$year-$monthString-$dayString"
                val person = (100000..900000).random()
                if (generatedEntries.find { it.id == id } == null) {
                    val generatedEntry = ForecastTimeEntry()
                    generatedEntry.id = id
                    generatedEntry.person = person
                    generatedEntry.nonProjectTime = if ((0..1).random() != 0) 1 else null
                    generatedEntry.timeRegistered = (1..435).random()
                    generatedEntry.date = date
                    generatedEntry.createdBy = person
                    generatedEntry.updatedBy = person
                    generatedEntry.createdAt = "${date}T12:00:00+02:00"
                    generatedEntry.updatedAt = "${date}T12:00:00+02:00"
                    generatedEntries.add(generatedEntry
                    )
                } else {
                    continue
                }
                id++
            }
            return generatedEntries
        }
    }
}