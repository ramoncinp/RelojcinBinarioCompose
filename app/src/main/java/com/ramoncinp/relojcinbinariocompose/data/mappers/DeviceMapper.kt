package com.ramoncinp.relojcinbinariocompose.data.mappers

import com.ramoncinp.relojcinbinariocompose.data.models.MAX_BRIGHTNESS_VALUE

fun pwmValueToPercentage(pwmValue: Int) = pwmValue * 100 / MAX_BRIGHTNESS_VALUE
fun percentageToPwmValue(percentage: Int) = percentage * MAX_BRIGHTNESS_VALUE / 100
