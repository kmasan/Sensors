package com.b22706.sensors

import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.b22706.sensors.sensors.Acceleration
import com.b22706.sensors.sensors.MyAudioSensor
import com.b22706.sensors.ui.theme.SensorsTheme
import com.kmasan.audiosensor.AudioSensor

class MainActivity : ComponentActivity() {
    lateinit var audioSensor: MyAudioSensor
    lateinit var acceleration: Acceleration
    lateinit var externalFilePath: String

    var sensorRun = false
    var csvRun = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createSetContent()

        externalFilePath = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString()

        audioSensor = MyAudioSensor(this)
        acceleration = Acceleration(this).apply { init() }
    }

    fun sensorStart(){
        acceleration.start()
        audioSensor.start(10)

        sensorRun = true
    }

    fun sensorStop(){
        sensorRun = false

        acceleration.stop()
        audioSensor.stop()
    }

    fun csvStart(fileName: String){
        acceleration.csvWriterStart(externalFilePath,fileName)
        audioSensor.csvWriterStart(externalFilePath,fileName)

        csvRun = true
    }

    fun csvStop(){
        csvRun = false
        acceleration.csvRun = false
        audioSensor.csvRun = false
    }

    private fun createSetContent(){
        setContent {
            var sensorButtonText by remember { mutableStateOf(" sensor off") }
            var csvButtonText by remember { mutableStateOf("csv start") }
            var accelerationText by remember { mutableStateOf("null") }
            var audioText by remember { mutableStateOf("null") }

            SensorsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Greeting("Android")
                        OnClickButton(text = sensorButtonText) {
                            sensorButtonText = when(sensorRun){
                                true->{
                                    sensorStop()
                                    "sensor off"
                                }
                                false->{
                                    sensorStart()
                                    "sensor on"
                                }
                            }
                        }
                        Button(onClick = {
                            csvButtonText = when(csvRun){
                                true->{
                                    csvStop()
                                    "csv start"
                                }
                                false->{
                                    csvStart("${System.currentTimeMillis()}")
                                    "csw writing"
                                }
                            }
                        }) {
                            Text(text = csvButtonText)
                        }
//                        Text(text = "acceleration")
//                        Text(text = accelerationText)
//                        Text(text = "audio")
//                        Text(text = accelerationText)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun OnClickButton(text: String, onClick: () -> Unit){
    Button(onClick = onClick
    ) {
        Text(text = text)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SensorsTheme {
        Greeting("Android")
    }
}