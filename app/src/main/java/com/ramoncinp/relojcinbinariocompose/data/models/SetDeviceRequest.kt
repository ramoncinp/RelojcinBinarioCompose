package com.ramoncinp.relojcinbinariocompose.data.models

data class SetDeviceRequest(
    val key: String = "set_data",
    val data: DeviceData
)
