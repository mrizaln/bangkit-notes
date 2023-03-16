package com.example.mydistrolist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Distro(
    val name: String,
    val description: String,
    val detail: String,
    val logo: String,
    val desktopPreview: String,
) : Parcelable
