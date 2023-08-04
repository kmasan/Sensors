package com.b22706.sensors

import android.graphics.PointF
import android.graphics.RectF
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.b22706.sensors.sensors.Acceleration
import com.b22706.sensors.sensors.MyAudioSensor
import com.b22706.sensors.ui.theme.SensorsTheme
import java.util.Date

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels{
        MainViewModel.factory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val sensorButtonText by remember { viewModel.sensorButtonText }
            val csvButtonText by remember { viewModel.csvButtonText }
            val sensorRun by remember { viewModel.sensorRun }
            val csvRun by remember { viewModel.csvRun }

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
                        SensorViewer(sensorType = "acceleration", liveData = viewModel.accelerationText)
                        TextButton(text = sensorButtonText) {
                            viewModel.sensorRun.value = when(sensorRun){
                                true->{
                                    viewModel.stopSensors()
                                    viewModel.sensorButtonText.value = "sensor start"
                                    false
                                }
                                false->{
                                    viewModel.startSensors()
                                    viewModel.sensorButtonText.value = "sensor stop"
                                    true
                                }
                            }
                        }
                        TextButton(text = csvButtonText){
                            viewModel.csvRun.value = when(csvRun){
                                true->{
                                    viewModel.stopCSV()
                                    viewModel.csvButtonText.value = "csv start"
                                    false
                                }
                                false->{
                                    viewModel.startCSV("${System.currentTimeMillis()}")
                                    viewModel.csvButtonText.value = "csw writing"
                                    true
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun TextButton(text: String, onClick: () -> Unit){
        Button(onClick = onClick
        ) {
            Text(text = text)
        }
    }

    @Composable
    fun SensorViewer(sensorType: String, liveData: MutableState<String>){
        val data by remember { liveData }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text("$sensorType: $data")
//        Graph(data = listOf(0f,0f))
        }
    }
}



@Composable
fun Graph(data: List<Float>){
    Canvas(
        modifier = Modifier,
//            .pointerInteropFilter { event ->
//                return@pointerInteropFilter when (event.action) {
//                    MotionEvent.ACTION_DOWN,
//                    MotionEvent.ACTION_POINTER_DOWN,
//                    MotionEvent.ACTION_MOVE -> {
//                        val pointedTime = (start.time + (end.time - start.time) * event.x / width).toLong()
//                        selectedLog = logs.minBy { abs(it.date.time - pointedTime) }
//                    }
//                    MotionEvent.ACTION_UP,
//                    MotionEvent.ACTION_POINTER_UP -> {
//                        selectedLog = null
//                        true
//                    }
//                    else -> false
//                }
//            },
        onDraw = {
//            val width = size.width // Canvas 自体の横幅。雑だがこうして取得するのが楽だった
            drawLine(Color.White, Offset(0f, 0f), Offset(1f, 1f))
            data.let {
//                val top = getPlot(size, graphPadding, 1f, 0f, 1f, it, 0, end)
//                val bottom = getPlot(size, graphPadding, 0f, 0f, 1f, it, start, end)
                drawLine(Color.White, Offset(0f, 0f), Offset(1000f, 1000f))
            }
        }
    )
}

fun getPlot(
    canvasSize: Size,
    padding: RectF,
    value: Float,
    maxValue: Float,
    minValue: Float,
    time: Date,
    start: Date,
    end: Date
): PointF {
    val paddingLeft = padding.left
    val paddingBottom = padding.bottom
    val width = canvasSize.width - (paddingLeft + padding.right)
    val height = canvasSize.height - (padding.top + paddingBottom)
    val timeRange = end.time - start.time
    val timeOffset = time.time - start.time
    val valueRange = maxValue - minValue
    val valueOffset = maxValue - value

    return PointF(paddingLeft + width * timeOffset / timeRange, paddingBottom + height * valueOffset / valueRange)
}