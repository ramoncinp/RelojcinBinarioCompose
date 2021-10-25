package com.ramoncinp.relojcinbinariocompose.ui.config

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramoncinp.relojcinbinariocompose.data.models.DeviceData

@Composable
fun DeviceConfigurationScreen(navController: NavController) {
    val viewModel: ConfigDeviceViewModel = hiltViewModel()
    val isLoading = viewModel.isLoading.observeAsState(initial = false)
    val connectedDevices = viewModel.connectedDevices.observeAsState(listOf())
    val selectedDevice = viewModel.selectedDevice.observeAsState(DeviceData())

    Scaffold(
        topBar = {
            AppBar(title = "Device Configuration", icon = Icons.Default.ArrowBack) {
                navController.navigateUp()
            }
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            if (isLoading.value) {
                ShowProgress()
            } else {
                if (connectedDevices.value.isEmpty()) {
                    NoDevicesMessage()
                } else {
                    DeviceConfigContent(selectedDevice.value, viewModel)
                }
            }
        }
    }
}

@Composable
fun DeviceConfigContent(device: DeviceData, viewModel: ConfigDeviceViewModel) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        TextField(
            value = device.ssid,
            onValueChange = { newVal -> viewModel.editDevice(device.copy(ssid = newVal)) },
            label = { Text("SSID") },
            placeholder = { Text("SSID") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        TextField(
            value = device.pass,
            onValueChange = { newVal -> viewModel.editDevice(device.copy(pass = newVal)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("Password") },
            placeholder = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        Row(modifier = Modifier.padding(bottom = 16.dp)) {
            Switch(
                checked = device.alarm,
                onCheckedChange = { newVal -> viewModel.editDevice(device.copy(alarm = newVal)) })
            Text("Alarm", modifier = Modifier.padding(start = 24.dp))
        }

        if (device.alarm) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = "",
                    onValueChange = { },
                    label = { Text("Hour") },
                    placeholder = { Text("Hour") },
                    modifier = Modifier
                        .width(180.dp)
                        .padding(end = 16.dp)
                )
                TextField(
                    value = "",
                    onValueChange = { },
                    label = { Text("Minute") },
                    placeholder = { Text("Minute") }
                )
            }
        }
    }
}

@Composable
fun AppBar(title: String, icon: ImageVector, navigationClickAction: () -> Unit) {
    TopAppBar(
        title = { Text(text = title) },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = Color.White,
        navigationIcon = {
            Icon(
                icon,
                "Navigation Icon",
                modifier = Modifier
                    .padding(12.dp)
                    .clickable { navigationClickAction.invoke() }
            )
        }
    )
}

@Composable
fun ShowProgress() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun NoDevicesMessage() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text("No devices", style = MaterialTheme.typography.h5)
    }
}
