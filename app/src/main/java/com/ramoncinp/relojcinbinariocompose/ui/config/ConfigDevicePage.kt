package com.ramoncinp.relojcinbinariocompose.ui.config

import androidx.compose.foundation.background
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramoncinp.relojcinbinariocompose.R
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
            AppBar(title = stringResource(R.string.device_conf), icon = Icons.Default.ArrowBack) {
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
                    DeviceConfigContent(selectedDevice.value, navController)
                }
            }
        }
    }
}

@Composable
fun DeviceConfigContent(
    device: DeviceData,
    navController: NavController,
    viewModel: ConfigDeviceViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        TextField(
            value = device.ssid,
            onValueChange = { newVal -> viewModel.editDevice(device.copy(ssid = newVal)) },
            label = { Text(stringResource(R.string.ssid)) },
            placeholder = { Text(stringResource(R.string.ssid)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        TextField(
            value = device.pass,
            onValueChange = { newVal -> viewModel.editDevice(device.copy(pass = newVal)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            label = { Text(stringResource(R.string.password)) },
            placeholder = { Text(stringResource(R.string.password)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        TimeZoneEditor(device)

        Row(modifier = Modifier.padding(bottom = 16.dp)) {
            Switch(
                checked = device.alarm,
                onCheckedChange = { newVal -> viewModel.editDevice(device.copy(alarm = newVal)) }
            )
            Text(stringResource(R.string.alarm), modifier = Modifier.padding(start = 24.dp))
        }

        if (device.alarm) {
            AlarmTimeRow(device)
        }

        BrightnessEditor()
        SubmitButtons()
        ActionsButtons(navController)
    }
}

@Composable
fun TimeZoneEditor(device: DeviceData, viewModel: ConfigDeviceViewModel = hiltViewModel()) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = device.hourZone.toString(),
            onValueChange = { },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(stringResource(R.string.time_zone)) },
            placeholder = { Text(stringResource(R.string.time_zone)) },
            modifier = Modifier.weight(4f)
        )

        Spacer(modifier = Modifier.size(16.dp))

        IconButton(
            onClick = { viewModel.subtractToTimeZone() },
            modifier = Modifier
                .padding(16.dp)
                .border(2.dp, MaterialTheme.colors.primary, shape = CircleShape)
                .background(MaterialTheme.colors.surface)
                .weight(1f)
        ) {
            Icon(
                Icons.Default.KeyboardArrowDown,
                contentDescription = stringResource(R.string.substract_time_zone)
            )
        }

        IconButton(
            onClick = { viewModel.addToTimeZone() },
            modifier = Modifier
                .padding(16.dp)
                .border(2.dp, MaterialTheme.colors.primary, shape = CircleShape)
                .background(MaterialTheme.colors.surface)
                .weight(1f)
        ) {
            Icon(
                Icons.Default.KeyboardArrowUp,
                contentDescription = stringResource(R.string.add_time_zone)
            )
        }
    }
}

@Composable
fun AlarmTimeRow(device: DeviceData, viewModel: ConfigDeviceViewModel = hiltViewModel()) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            value = device.alarmHour,
            onValueChange = { newVal -> viewModel.editDevice(device.copy(alarmHour = newVal)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(stringResource(R.string.hour)) },
            placeholder = { Text(stringResource(R.string.hour)) },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(16.dp))
        TextField(
            value = device.alarmMinute,
            onValueChange = { newVal -> viewModel.editDevice(device.copy(alarmMinute = newVal)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(stringResource(R.string.minute)) },
            placeholder = { Text(stringResource(R.string.minute)) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun BrightnessEditor(viewModel: ConfigDeviceViewModel = hiltViewModel()) {
    val brightnessValue = viewModel.brightnessPercentage.observeAsState(initial = 0f)

    Column {
        Text(
            text = stringResource(R.string.brightness),
            modifier = Modifier.padding(bottom = 8.dp, top = 16.dp)
        )
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
                    contentDescription = stringResource(R.string.set_brightness)
                )
            }
        }
    }
}

@Composable
fun SubmitButtons(viewModel: ConfigDeviceViewModel = hiltViewModel()) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    )
    {
        Button(onClick = { viewModel.setDeviceData() }, contentPadding = PaddingValues(16.dp)) {
            Icon(
                Icons.Filled.Send,
                contentDescription = stringResource(R.string.save_form_data),
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(stringResource(R.string.save))
        }
        Spacer(modifier = Modifier.size(16.dp))
        Button(onClick = { viewModel.playSong() }, contentPadding = PaddingValues(16.dp)) {
            Icon(
                Icons.Filled.PlayArrow,
                contentDescription = stringResource(R.string.play_sound),
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(stringResource(R.string.play))
        }
        Spacer(modifier = Modifier.size(16.dp))
        Button(onClick = { viewModel.stopSong() }, contentPadding = PaddingValues(16.dp)) {
            Icon(
                Icons.Filled.Clear,
                contentDescription = stringResource(R.string.stop_sound),
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(stringResource(R.string.stop_sound))
        }
    }
}

@Composable
fun ActionsButtons(
    navController: NavController,
    viewModel: ConfigDeviceViewModel = hiltViewModel()
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    )
    {
        Button(
            onClick = {
                viewModel.rebootDevice()
                navController.navigateUp()
            },
            contentPadding = PaddingValues(16.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Red,
                contentColor = Color.White
            )
        ) {
            Text(stringResource(R.string.reboot))
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
                stringResource(R.string.nav_icon),
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
        Text(stringResource(R.string.no_devices), style = MaterialTheme.typography.h5)
    }
}
