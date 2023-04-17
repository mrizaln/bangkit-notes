package com.example.mysound

import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private val sp by lazy { SoundPool.Builder().setMaxStreams(10).build() }
    private var soundIds = listOf<Int>()
    private var spLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sp.setOnLoadCompleteListener { _, _, status ->
            spLoaded = if (status == 0) {
                true
            } else {
                Toast.makeText(this@MainActivity, "Gagal load", Toast.LENGTH_SHORT).show()
                false
            }
        }

        soundIds = audios.map { sp.load(this, it, 1) }

        val btnSound = findViewById<Button>(R.id.btn_sound_pool)
        btnSound.setOnClickListener {
            if (spLoaded) {
                sp.play(soundIds.random(), 1f, 1f, 0, 0, 1f)
            }
        }
    }

    override fun onDestroy() {
        soundIds.forEach { sp.unload(it) }
        super.onDestroy()
    }

    companion object {
        private val audios = listOf(R.raw.rushia, R.raw.pekora, R.raw.ayame)
    }
}