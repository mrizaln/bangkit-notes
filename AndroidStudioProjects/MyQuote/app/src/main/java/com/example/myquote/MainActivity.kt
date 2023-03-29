package com.example.myquote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.myquote.databinding.ActivityMainBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = MainActivity::class.simpleName
    }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getRandomQuote()

        binding.btnAllQuotes.setOnClickListener {
            startActivity(Intent(this@MainActivity, ListQuotesActivity::class.java))
        }
    }

    private fun getRandomQuote() {
        binding.progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://quote-api.dicoding.dev/random"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
            ) {
                binding.progressBar.visibility = View.INVISIBLE

                if (responseBody!= null) {
                    val result = String(responseBody)
                    Log.d(TAG, result)
                    try {
                        val responseObject = JSONObject(result)
                        val quote = responseObject.getString("en")
                        val author = responseObject.getString("author")

                        binding.tvQuote.text = quote
                        binding.tvAuthor.text = author
                    } catch (e: Exception) {
                        Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?,
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = "$statusCode: " + when (statusCode) {
                    401 -> "Bad Request"
                    403 -> "Forbidden"
                    404 -> "Not Found"
                    else -> "${error?.message ?: "noerror"}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}