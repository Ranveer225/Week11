package edu.farmingdale.threadsexample.countdowntimer

import android.app.Application
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.farmingdale.threadsexample.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerViewModel(application: Application) : AndroidViewModel(application) {
    private var timerJob: Job? = null

    var selectedHour by mutableIntStateOf(0)
        private set
    var selectedMinute by mutableIntStateOf(0)
        private set
    var selectedSecond by mutableIntStateOf(0)
        private set

    var totalMillis by mutableLongStateOf(0L)
        private set
    var remainingMillis by mutableLongStateOf(0L)
        private set

    var isRunning by mutableStateOf(false)
        private set

    fun selectTime(hour: Int, min: Int, sec: Int) {
        selectedHour = hour
        selectedMinute = min
        selectedSecond = sec
    }

    fun startTimer() {
        totalMillis = (selectedHour * 60 * 60 + selectedMinute * 60 + selectedSecond) * 1000L

        if (totalMillis > 0) {
            isRunning = true
            remainingMillis = totalMillis

            timerJob = viewModelScope.launch {
                while (remainingMillis > 0) {
                    delay(1000)
                    remainingMillis -= 1000
                }

                isRunning = false
            }
        }
    }

    fun cancelTimer() {
        if (isRunning) {
            timerJob?.cancel()
            isRunning = false
            remainingMillis = 0
        }
    }

    fun resetTimer() {
        cancelTimer()
        remainingMillis = totalMillis
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}