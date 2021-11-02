package com.ramoncinp.relojcinbinariocompose.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ramoncinp.relojcinbinariocompose.data.models.BcdBinaryTime
import com.ramoncinp.relojcinbinariocompose.ui.shapes.Circle

@Composable
fun BinaryClock(bcdBinaryTime: BcdBinaryTime) {
    LazyRow(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly) {
        items(bcdBinaryTime.timeElements) { timeElement ->
            BinaryColumn(timeElement)
        }
    }
}

@Composable
fun BinaryColumn(binaryNibble: String) {
    LazyColumn(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        items(binaryNibble.toList()) { binaryChar ->
            Circle(binaryChar == '1')
        }
    }
}
