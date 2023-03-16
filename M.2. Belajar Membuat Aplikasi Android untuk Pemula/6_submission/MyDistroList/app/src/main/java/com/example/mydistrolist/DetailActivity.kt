package com.example.mydistrolist

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mydistrolist.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val distro = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(ListDistroAdapter.KEY_DISTRO, Distro::class.java)
        } else {
            @Suppress("DEPRECATION") intent.getParcelableExtra(ListDistroAdapter.KEY_DISTRO)
        }

        if (distro != null) {
            supportActionBar?.title = distro.name

            Glide.with(this@DetailActivity).load(distro.logo).into(binding.imgLogo)
            Glide.with(this@DetailActivity).load(distro.desktopPreview)
                .into(binding.imgDesktopPreview)

            val (basedOn, origin, architecture, desktop, category) = distro.detail.split(';')
            binding.apply {
                tvBasedOnValue.text = basedOn
                tvOriginValue.text = origin
                tvArchitectureValue.text = architecture
                tvDesktopValue.text = desktop
                tvCategoryValue.text = category
                tvDescription.text = distro.description

                actionShare.setOnClickListener {
                    val share = Intent(Intent.ACTION_SEND)
                    share.type = "text/plain"
                    share.putExtra(Intent.EXTRA_TEXT, distro.description)
                    startActivity(Intent.createChooser(share, "Share Link"))
                    Toast.makeText(
                        this@DetailActivity, "Sharing " + distro.name, Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}