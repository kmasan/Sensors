package com.b22706.sensors

import android.app.Activity
import android.content.Context
import android.os.Environment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.b22706.sensors.sensors.Acceleration
import com.b22706.sensors.sensors.Gyroscope
import com.b22706.sensors.sensors.MyAudioSensor
import com.b22706.sensors.sensors.SensorBase
import com.kmasan.audiosensor.AudioSensor

class MainViewModel(
    val externalFilePath: String,
    val acceleration: Acceleration,
    val gyroscope: Gyroscope,
    val audioSensor: MyAudioSensor
    ) : ViewModel()
{
    var sensorRun = false
    val sensorButtonText = mutableStateOf("sensor off")
    var csvRun = false
    val csvButtonText = mutableStateOf("csv start")

    val accelerationText = acceleration.dataText
    val gyroscopeText = gyroscope.dataText
    val audioText = mutableStateOf("null")

    fun switchRun(){
        sensorRun = when(sensorRun){
            true->{
                stopSensors()
                sensorButtonText.value = "sensor start"
                false
            }
            false->{
                startSensors()
                sensorButtonText.value = "sensor stop"
                true
            }
        }
    }
    fun startSensors(){
        acceleration.start()
        gyroscope.start()
//        audioSensor.start(10)
    }

    fun stopSensors(){
        acceleration.stop()
        gyroscope.stop()
//        audioSensor.stop()
    }

    fun switchCSVRun(){
        csvRun = when(csvRun){
            true->{
                stopCSV()
                csvButtonText.value = "csv start"
                false
            }
            false->{
                startCSV("${System.currentTimeMillis()}")
                csvButtonText.value = "csw writing"
                true
            }
        }
    }

    fun startCSV(fileName: String){
        acceleration.csvWriterStart(externalFilePath, fileName)
        gyroscope.csvWriterStart(externalFilePath, fileName)
    }

    fun stopCSV(){
        acceleration.csvRun = false
        gyroscope.csvRun = false
    }

    companion object{
        fun factory(context: Context): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            val externalFilePath = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString()
            val acceleration = Acceleration.create(context)
            val gyroscope = Gyroscope.create(context)
            val audioSensor = MyAudioSensor(context)
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>
            ): T {
                return MainViewModel(externalFilePath, acceleration, gyroscope, audioSensor) as T
            }
        }
    }
}