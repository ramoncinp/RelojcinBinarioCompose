package com.ramoncinp.relojcinbinariocompose.data.mappers

import com.ramoncinp.relojcinbinariocompose.data.BcdBinaryTime
import com.ramoncinp.relojcinbinariocompose.domain.toBinaryNibbleString

const val TIME_ELEMENT_LENGTH = 2
const val HOUR_INDEX = 0
const val MINUTE_INDEX = 2
const val SECOND_INDEX = 4

class BcdBinaryTimeMapper {

    /**
     * input: Date formated as "HHmmss" like "153032" for 15:30:32
     */
    fun fromFormattedTime(formattedDate: String): BcdBinaryTime {
        val hour = getTimeElementFromString(formattedDate, HOUR_INDEX)
        val minutes = getTimeElementFromString(formattedDate, MINUTE_INDEX)
        val seconds = getTimeElementFromString(formattedDate, SECOND_INDEX)

        val hourPair = getTensAndUnits(hour)
        val minutesPair = getTensAndUnits(minutes)
        val secondsPair = getTensAndUnits(seconds)

        val listOfResults = mutableListOf<Int>()
        listOfResults.addAll(hourPair.toList())
        listOfResults.addAll(minutesPair.toList())
        listOfResults.addAll(secondsPair.toList())

        val binaryResults = listOfResults.map { intResult ->
            intResult.toBinaryNibbleString()
        }

        return BcdBinaryTime(binaryResults)
    }

    private fun getTimeElementFromString(formattedDate: String, startIndex: Int) =
        formattedDate.substring(startIndex, startIndex + TIME_ELEMENT_LENGTH).toInt()

    private fun getTensAndUnits(intValue: Int): Pair<Int, Int> {
        val tensVal = intValue / 10
        val unitsVal = intValue % 10
        return Pair(tensVal, unitsVal)
    }
}
