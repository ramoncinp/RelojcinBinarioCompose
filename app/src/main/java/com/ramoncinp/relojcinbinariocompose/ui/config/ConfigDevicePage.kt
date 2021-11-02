package com.ramoncinp.relojcinbinariocompose.ui.config

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
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
    val snackbarHostState = remember { SnackbarHostState() }
    val viewModel: ConfigDeviceViewModel = hiltViewModel()

    val isLoading = viewModel.isLoading.observeAsState(initial = false)
    val connectedDevices = viewModel.connectedDevices.observeAsState(listOf())
    val selectedDevice = viewModel.selectedDevice.observeAsState(DeviceData())
    val message = viewModel.message.observeAsState("")

    LaunchedEffect(message.value) {
        if (message.value.isNotEmpty()) {
            snackbarHostState.showSnackbar(message.value)
            viewModel.onMessageShowed()
        }
    }

    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState),
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

        TimeZoneEditor(device, viewModel)

        Row(modifier = Modifier.padding(bottom = 16.dp)) {
            Switch(
                checked = device.alarm,
                onCheckedChange = { newVal -> viewModel.editDevice(device.copy(alarm = newVal)) })
            Text("Alarm", modifier = Modifier.padding(start = 24.dp))
        }

        if (device.alarm) {
            AlarmTimeRow(device, viewModel)
        }

        BrightnessEditor(viewModel)
        SubmitButtons(viewModel)
        ActionsButtons(viewModel)
    }
}

@Composable
fun TimeZoneEditor(device: DeviceData, viewModel: ConfigDeviceViewModel) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        TextField(
            value = device.hourZone.toString(),
            onValueChange = { },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text("Time zone") },
            placeholder = { Text("Time zone") },
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.size(16.dp))

        IconButton(
            onClick = { viewModel.subtractToTimeZone() },
            modifier = Modifier
                .padding(16.dp)
                .border(2.dp, MaterialTheme.colors.primary, shape = CircleShape)
        ) {
            Icon(
                Icons.Default.KeyboardArrowDown,
                contentDescription = "Subtract to time zone"
            )
        }

        Spacer(modifier = Modifier.size(8.dp))

        IconButton(
            onClick = { viewModel.addToTimeZone() },
            modifier = Modifier
                .padding(16.dp)
                .border(2.dp, MaterialTheme.colors.primary, shape = CircleShape)
        ) {
            Icon(
                Icons.Default.KeyboardArrowUp,
                contentDescription = "Add to time zone"
            )
        }
    }
}

@Composable
fun AlarmTimeRow(device: DeviceData, viewModel: ConfigDeviceViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            value = device.alarmHour.toString(),
            onValueChange = { newVal ->
                if (newVal.isNotEmpty())
                    viewModel.editDevice(device.copy(alarmHour = newVal.toInt()))
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text("Hour") },
            placeholder = { Text("Hour") },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(16.dp))
        TextField(
            value = device.alarmMinute.toString(),
            onValueChange = { newVal ->
                if (newVal.isNotEmpty())
                    viewModel.editDevice(device.copy(alarmMinute = newVal.toInt()))
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text("Minute") },
            placeholder = { Text("Minute") },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun BrightnessEditor(viewModel: ConfigDeviceViewModel) {
    val brightnessValue = viewModel.brightnessPercentage.observeAsState(initial = 0f)

    Column {
        Text(text = "Brightness", modifier = Modifier.padding(bottom = 8.dp, top = 16.dp))
        Row(modifier = Modifier.padding(end = 16.dp)) {
            Slider(
                value = brightnessValue.value,
                onValueChange = { newPercentage -> viewModel.setBrightnessValue(newPercentage) },
                Modifier.padding(end = 24.dp)
            )
            IconButton(
                modifier = Modifier.requiredWidth(64.dp),
                onClick = { viewModel.sendNewBrightnessValue() }) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Set brightness"
                )
            }
        }
    }
}

@Composable
fun SubmitButtons(viewModel: ConfigDeviceViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    )
    {
        Button(onClick = { viewModel.setDeviceData() }, contentPadding = PaddingValues(16.dp)) {
            Icon(
                Icons.Filled.Send,
                contentDescription = "Save form data",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Save")
        }
        Spacer(modifier = Modifier.size(16.dp))
        Button(onClick = { }, contentPadding = PaddingValues(16.dp)) {
            Icon(
                Icons.Filled.Refresh,
                contentDescription = "Sync time",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Sync Time")
        }
    }
}

@Composable
fun ActionsButtons(viewModel: ConfigDeviceViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    )
    {
        Button(onClick = { viewModel.playSong() }, contentPadding = PaddingValues(16.dp)) {
            Icon(
                Icons.Filled.PlayArrow,
                contentDescription = "Play sound",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Play")
        }
        Spacer(modifier = Modifier.size(16.dp))
        Button(onClick = { viewModel.stopSong() }, contentPadding = PaddingValues(16.dp)) {
            Icon(
                Icons.Filled.Clear,
                contentDescription = "Stop sound",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Stop sound")
        }
        Spacer(modifier = Modifier.size(16.dp))
        Button(
            onClick = { viewModel.rebootDevice() },
            contentPadding = PaddingValues(16.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Red,
                contentColor = Color.White
            )
        ) {
            Text("Reboot")
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
