package com.b22706.sensors.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.FileWriter
import java.io.IOException

abstract class SensorBase(private val context: Context): SensorEventListener {
    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    var sensor: Sensor? = null
    var queue: ArrayDeque<String> = ArrayDeque(listOf())
    val dataText = mutableStateOf("null")
    var run = false
    var csvRun = false

    abstract val sensorType: Int
    abstract val sensorName: String
    abstract val sensorDelay: Int
    abstract val csvHeader: String

    fun init(){
        sensor = sensorManager.getDefaultSensor(sensorType)
    }

    fun start(){
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME)
        run = true
    }

    fun stop(){
        run = false
        if(csvRun) csvRun = false
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
    }

    override fun onAccuracyChanged(sensor: Sensor?, p1: Int) {
    }

    fun csvWriterStart(path: String, fileName: String): Boolean {
        //CSVファイルの書き出し
        try{
            //書込み先指定
            val writer = FileWriter("$path/$fileName-$sensorName.csv")

            //書き込み準備
            val csvPrinter = CSVPrinter(
                writer,
                CSVFormat.DEFAULT.withHeader(csvHeader)
            )
            val hnd = Handler(Looper.getMainLooper())
            queue.clear()
            csvRun = true
            // こいつ(rnb0) が何回も呼ばれる
            val rnb = object : Runnable {
                override fun run() {
//                    val queueClone = queue
                    //書き込み開始
                    for(data in queue){
                        //データ保存
                        csvPrinter.printRecord(
                            data
                        )
                    }
                    queue.clear()

                    // stop用のフラグ
                    when(csvRun) {
                        true -> {
                            // 指定時間後に自分自身を呼ぶ
                            hnd.postDelayed(this, 1000)
                        }
                        false -> {
                            //データ保存の終了処理
                            csvPrinter.flush()
                            csvPrinter.close()
                        }
                    }
                }
            }
            // 初回の呼び出し
            hnd.post(rnb)
            return true
        }catch (e: IOException){
            //エラー処理d
            Log.d("csvWrite", "${e}:${e.message!!}")
            return false
        }
    }
}