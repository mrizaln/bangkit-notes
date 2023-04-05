package com.example.myworkmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.SyncHttpClient
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.text.DecimalFormat

class MyWorker(
    context: Context,
    workerParameters: WorkerParameters,
) : Worker(context, workerParameters) {
    companion object {
        private val TAG = this::class.java.simpleName
        const val APP_ID = BuildConfig.API_KEY
        const val EXTRA_CITY = "city"
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "channel_01"
        const val CHANNEL_NAME = "dicoding channel"
    }

    private var resultStatus: Result? = null

    override fun doWork(): Result {
        val dataCity = inputData.getString(EXTRA_CITY)!!
        return getCurrentWeather(dataCity)
    }

    private fun getCurrentWeather(city: String): Result {
        Log.d(TAG, "getCurrentWeather: Mulai...")
        Looper.prepare()
        val client = SyncHttpClient()
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$APP_ID"
        Log.d(TAG, "getCurrentWeather: $url")
        client.post(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray,
            ) {
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
//                    val responseObject = JSONObject(result)
//                    val currentWeather =
//                        responseObject.getJSONArray("weather").getJSONObject(0).getString("main")
//                    val description = responseObject.getJSONArray("weather").getJSONObject(0)
//                        .getString("description")
//                    val tempInKelvin = responseObject.getJSONObject("main").getDouble("temp")
//                    val tempInCelcius = tempInKelvin - 273.15
//                    val temperature = DecimalFormat("##.##").format(tempInCelcius)
//                    val title = "Current Weather in $city"
//                    val message = "$currentWeather, $description with $temperature celcius"
//                    showNotification(title, message)

                    val moshi = Moshi.Builder()
                        .addLast(KotlinJsonAdapterFactory())
                        .build()

                    val jsonAdapter = moshi.adapter(OpenWeatherMapResponse::class.java)
                    val response = jsonAdapter.fromJson(result)

                    response?.let {
                        val currentWeather = it.weather?.get(0)?.main!!
                        val description = it.weather[0]?.description!!
                        val tempInKelvin = it.main?.temp!!
                        val tempInCelcius = tempInKelvin - 273.15
                        val temperature = DecimalFormat("##.##").format(tempInCelcius)
                        val title = "Current Weather in $city"
                        val message = "$currentWeather, $description with $temperature celcius"
                        showNotification(title, message)
                    }

                    Log.d(TAG, "onSuccess: Selesai...")
                    resultStatus = Result.success()
                } catch (e: Exception) {
                    showNotification("Get Current Weather Not Success", e.message ?: "error")
                    Log.d(TAG, "onSuccess: Gagal...")
                    resultStatus = Result.failure()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?,
            ) {
                Log.d(TAG, "onFailure: Gagal...")
                showNotification("Get Current Weather Failed", error?.message ?: "error")
                resultStatus = Result.failure()
            }
        })
        return resultStatus!!
    }

    private fun showNotification(title: String, description: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_black_24dp)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notification.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }
}