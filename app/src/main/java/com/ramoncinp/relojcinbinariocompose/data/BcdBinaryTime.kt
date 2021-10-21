package com.ramoncinp.relojcinbinariocompose.data

data class BcdBinaryTime(val timeElements: List<String>)

fun getInitialBinaryTime() = BcdBinaryTime(
    listOf("0001", "0010", "0000", "0000", "0000", "0000")
)
