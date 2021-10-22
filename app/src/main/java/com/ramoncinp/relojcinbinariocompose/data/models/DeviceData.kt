package com.ramoncinp.relojcinbinariocompose.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetDeviceDataResponse(
    val message: String,
    val status: String
)

@JsonClass(generateAdapter = true)
data class DeviceData(
    var ssid: String = "",
    var pass: String = "",
    var alarm: Boolean = false,
    @Json(name = "alarm_hour") var alarmHour: Int = 0,
    @Json(name = "alarm_minute") var alarmMinute: Int = 0,
    @Json(name = "pwm_value") var pwmValue: Int = 0,
    @Json(name = "hzone") var hourZone: Int = 0,
)
