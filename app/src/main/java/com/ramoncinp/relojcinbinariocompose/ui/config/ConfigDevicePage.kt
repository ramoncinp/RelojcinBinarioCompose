package com.ramoncinp.relojcinbinariocompose.ui.config

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
                    DeviceConfigContent(selectedDevice.value)
                }
            }
        }
    }
}

@Composable
fun DeviceConfigContent(device: DeviceData) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(device.ssid, style = MaterialTheme.typography.h6, modifier = Modifier.padding(16.dp))
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
