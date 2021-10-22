package com.ramoncinp.relojcinbinariocompose.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramoncinp.relojcinbinariocompose.data.mappers.BcdBinaryTimeMapper
import com.ramoncinp.relojcinbinariocompose.data.models.BcdBinaryTime
import com.ramoncinp.relojcinbinariocompose.domain.dateFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class MainViewModel : ViewModel() {

    private val _currentBinaryTime = MutableLiveData<BcdBinaryTime>()
    val currentBinaryTime: LiveData<BcdBinaryTime>
        get() = _currentBinaryTime

    init {
        startTimeTask()
    }

    private fun startTimeTask() = viewModelScope.launch(Dispatchers.IO) {
        while (true) {
            val formattedTime = getTime()
            getBinaryTime(formattedTime)
            delay(1000)
        }
    }

    private fun getBinaryTime(formattedDate: String) {
        val binaryTime = BcdBinaryTimeMapper().fromFormattedTime(formattedDate)
        _currentBinaryTime.postValue(binaryTime)
    }

    private fun getTime(): String {
        val now = Date()
        val formattedDate = dateFormatter.format(now)
        Timber.d(formattedDate)
        return formattedDate
    }
}
