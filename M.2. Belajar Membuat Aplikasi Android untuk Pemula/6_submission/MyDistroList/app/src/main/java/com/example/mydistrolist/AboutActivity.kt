package com.example.mydistrolist

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.mydistrolist.databinding.ActivityAboutBinding


class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding
    private lateinit var profilePicture: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "About Me"

        val (profilePicture_github, profilePicture_linkedin, logo_gmail, logo_github, logo_linkedin) = listOf(
            R.string.profile_picture_github,
            R.string.profile_picture_linkedin,
            R.string.logo_gmail,
            R.string.logo_github,
            R.string.logo_linkedin
        ).map {
            resources.getString(it)
        }

        profilePicture = profilePicture_linkedin
        Glide.with(this@AboutActivity).load(profilePicture).into(binding.imgProfile)
        binding.imgProfile.setOnClickListener {
            when (profilePicture) {
                profilePicture_github -> {
                    profilePicture = profilePicture_linkedin
                    Glide.with(this@AboutActivity).load(profilePicture).into(binding.imgProfile)
                }
                profilePicture_linkedin -> {
                    profilePicture = profilePicture_github
                    Glide.with(this@AboutActivity).load(profilePicture).into(binding.imgProfile)
                }
            }
        }

        listOf(
            Triple(binding.tvEmail, logo_gmail, Uri.parse("mailto:${binding.tvEmail.text}")),
            Triple(binding.tvGithub, logo_github, Uri.parse("https://github.com/${binding.tvGithub.text}")),
            Triple(binding.tvLinkedin, logo_linkedin, Uri.parse("https://linkedin.com/in/${binding.tvLinkedin.text}"))
        ).map { (view, imgUrl, intentUri) ->
            insertPictureIntoTextView(view, imgUrl)
            view.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, intentUri)) }
        }
    }

    private fun insertPictureIntoTextView(view: TextView, imgUrl: String) {
        Glide.with(this@AboutActivity).load(imgUrl).apply(RequestOptions().fitCenter())
            .into(object : CustomTarget<Drawable>(30, 30) {
                override fun onLoadCleared(placeholder: Drawable?) {
                    view.setCompoundDrawablesWithIntrinsicBounds(
                        placeholder, null, null, null
                    )
                }

                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?,
                ) {
                    view.setCompoundDrawablesWithIntrinsicBounds(
                        resource, null, null, null
                    )
                }
            })
    }
}