package com.ramoncinp.relojcinbinariocompose.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ramoncinp.relojcinbinariocompose.data.getInitialBinaryTime
import com.ramoncinp.relojcinbinariocompose.ui.BinaryClock
import com.ramoncinp.relojcinbinariocompose.ui.theme.RelojcinBinarioComposeTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RelojcinBinarioComposeTheme {
                MyApp()
            }
        }
        initObservers()
    }

    private fun initObservers() {
        viewModel.connectedDevices.observe(this) {
            if (it.isNotEmpty())
                Log.d("MainActivity", "Connected device ${it[0]}")
        }
    }
}

@Composable
fun AppBar(title: String) {
    val viewModel: MainViewModel = viewModel()
    TopAppBar(
        title = { Text(text = title) },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = Color.White,
        actions = {
            IconButton(onClick = { viewModel.scanForDevices() }) {
                Icon(Icons.Filled.Settings, contentDescription = "App settings", tint = Color.White)
            }
        }
    )
}

@Composable
fun MyApp() {
    val viewModel: MainViewModel = viewModel()
    val time = viewModel.currentBinaryTime.observeAsState(getInitialBinaryTime())
    val isLoading = viewModel.isLoading.observeAsState(false)

    Scaffold(
        topBar = { AppBar(title = "Binary Clock") }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            if (isLoading.value) {
                ShowProgress()
            } else {
                BinaryClock(time.value)
            }
        }
    }
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RelojcinBinarioComposeTheme {
        MyApp()
    }
}
