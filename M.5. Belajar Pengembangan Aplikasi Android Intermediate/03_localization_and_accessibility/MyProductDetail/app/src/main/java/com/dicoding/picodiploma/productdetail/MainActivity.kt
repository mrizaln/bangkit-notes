package com.dicoding.picodiploma.productdetail

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log.d
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.dicoding.picodiploma.productdetail.Helper.withCurrencyFormat
import com.dicoding.picodiploma.productdetail.Helper.withDateFormat
import com.dicoding.picodiploma.productdetail.Helper.withNumberingFormat
import com.dicoding.picodiploma.productdetail.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
        setupAction()
        setupData()
    }

    private fun setupView() {
        @Suppress("DEPRECATION") if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.settingImageView.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    private fun setupData() {
        val product = RemoteDataSource(this).getDetailProduct().apply {
            binding.apply {
                nameTextView.text = name
                storeTextView.text = store
                dateTextView.text = date
                sizeTextView.text = size
                colorTextView.text = color
                descTextView.text = desc

                priceTextView.text = price.withCurrencyFormat()

                ratingTextView.text = getString(
                    R.string.ratingFormat,
                    rating.withNumberingFormat(),
                    countRating.withNumberingFormat()
                )

                dateTextView.text = getString(R.string.dateFormat, date.withDateFormat())

                previewImageView.setImageResource(image)
            }
        }

        setupAccessibility(product)
    }

    private fun setupAccessibility(productModel: ProductModel) {
        val dr = { v: View, r: Int -> v.contentDescription = getString(r) }
        val ds = { v: View, s: String -> v.contentDescription = s }

        productModel.apply {
            binding.apply {
                dr(settingImageView, R.string.settingDescription)
                dr(previewImageView, R.string.previewDescription)

                ds(storeTextView, getString(R.string.storeDescription, store))
                ds(colorTextView, getString(R.string.colorDescription, color))
                ds(sizeTextView, getString(R.string.sizeDescription, size))

                ds(
                    ratingTextView, getString(
                        R.string.ratingDescription,
                        rating.withNumberingFormat(),
                        countRating.withNumberingFormat()
                    )
                )
            }
        }
    }
}