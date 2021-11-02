package com.ramoncinp.relojcinbinariocompose.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ramoncinp.relojcinbinariocompose.data.models.getInitialBinaryTime
import com.ramoncinp.relojcinbinariocompose.ui.BinaryClock
import com.ramoncinp.relojcinbinariocompose.ui.config.DeviceConfigurationScreen
import com.ramoncinp.relojcinbinariocompose.ui.theme.RelojcinBinarioComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RelojcinBinarioComposeTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun AppBar(title: String, actionOnClick: () -> Unit) {

    TopAppBar(
        title = { Text(text = title) },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = Color.White,
        actions = {
            IconButton(onClick = { actionOnClick.invoke() }) {
                Icon(Icons.Filled.Settings, contentDescription = "App settings", tint = Color.White)
            }
        })
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "binary_clock") {
        composable("binary_clock") { BinaryClockScreen(navController) }
        composable("device_config") { DeviceConfigurationScreen(navController) }
    }
}

@Composable
fun BinaryClockScreen(navController: NavController) {
    val viewModel: MainViewModel = viewModel()
    val time = viewModel.currentBinaryTime.observeAsState(getInitialBinaryTime())

    Scaffold(
        topBar = {
            AppBar(title = "Binary Clock") {
                navController.navigate("device_config")
            }
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            BinaryClock(time.value)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RelojcinBinarioComposeTheme {
        MyApp()
    }
}
