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
    @Json(name = "alarm_hour") var alarmHour: String = "",
    @Json(name = "alarm_minute") var alarmMinute: String = "",
    @Json(name = "bright_percent") var brightPercent: Int = 0,
    @Json(name = "hzone") var hourZone: Int = 0,
)
