package com.ramoncinp.relojcinbinariocompose.domain

fun Int.toBinaryNibbleString(): String =
    this.toString(2).padStart(4, '0')
