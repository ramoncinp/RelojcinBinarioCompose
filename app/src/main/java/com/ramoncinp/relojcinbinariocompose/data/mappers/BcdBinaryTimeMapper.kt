package com.ramoncinp.relojcinbinariocompose.data.mappers

import com.ramoncinp.relojcinbinariocompose.data.BcdBinaryTime
import com.ramoncinp.relojcinbinariocompose.domain.toBinaryNibbleString

class BcdBinaryTimeMapper {

    /**
     * input: Date formated as "HHmmss" like "153032" for 15:30:32
     */
    fun fromFormattedTime(formattedDate: String): BcdBinaryTime {
        val listOfResults = formattedDate.toList().map {
            it.toString().toInt()
        }

        val binaryResults = listOfResults.map { intResult ->
            intResult.toBinaryNibbleString()
        }

        return BcdBinaryTime(binaryResults)
    }
}
