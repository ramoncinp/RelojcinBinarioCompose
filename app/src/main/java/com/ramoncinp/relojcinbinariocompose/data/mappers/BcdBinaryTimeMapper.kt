package com.ramoncinp.relojcinbinariocompose.data.mappers

import com.ramoncinp.relojcinbinariocompose.data.models.BcdBinaryTime
import com.ramoncinp.relojcinbinariocompose.domain.toBinaryNibbleString

class BcdBinaryTimeMapper {

    /**
     * input: Date formatted as "HHmmss" like "153032" for 15:30:32
     */
    fun fromFormattedTime(formattedDate: String): BcdBinaryTime {
        val listOfCharacters = formattedDate.toList().map {
            it.toString().toInt()
        }

        val binaryNibbles = listOfCharacters.map { intResult ->
            intResult.toBinaryNibbleString()
        }

        return BcdBinaryTime(binaryNibbles)
    }
}
