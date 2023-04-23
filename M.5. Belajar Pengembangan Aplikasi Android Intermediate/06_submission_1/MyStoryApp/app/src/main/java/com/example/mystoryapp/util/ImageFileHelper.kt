package com.example.mystoryapp.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

object ImageFileHelper {
    private fun getTimestamp(): String {
        val format = "yyyy-MM-dd"
        return SimpleDateFormat(format, Locale.US).format(System.currentTimeMillis())
    }

    suspend fun reduceImageSize(file: File): File? {
        val STREAM_LENGTH_LIMIT = 1_000_000
        val DOWNSCALE_TRIES_LIMIT = 3

        var bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int = bitmap.byteCount
        var downscaleTries = 0
        val bmpStream = ByteArrayOutputStream()

        Log.d(
            "ImageFileHelper",
            "reduceImageSize begin (len: $streamLength, %: ${100 * (streamLength / STREAM_LENGTH_LIMIT.toDouble())})"
        )

        while (streamLength > STREAM_LENGTH_LIMIT) {
            if (compressQuality <= 0) {
                if (++downscaleTries >= DOWNSCALE_TRIES_LIMIT)
                    break

                val downscaleFactor = 0.5f          // this number was arbitrary chosen
                bitmap = downScaleBitmap(bitmap, downscaleFactor)
                compressQuality = 100

                Log.d(
                    "ImageFileHelper",
                    "reduceImageSize downscale (factor: $downscaleFactor, tries: $downscaleTries"
                )
            }

            bmpStream.reset()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)

            streamLength = bmpStream.toByteArray().size

            Log.d(
                "ImageFileHelper",
                "reduceImageSize, len: $streamLength, %: ${100 * (streamLength / STREAM_LENGTH_LIMIT.toDouble())}, compressQuality: $compressQuality"
            )

            val qualityDrop = (streamLength / STREAM_LENGTH_LIMIT.toDouble() - 1) + 5        // allow for faster diminish I guess
            compressQuality -= qualityDrop.toInt()
        }

        Log.d("ImageFileHelper", "reduceImageSize done")

        if (downscaleTries >= DOWNSCALE_TRIES_LIMIT) {
            return null
        }

        withContext(Dispatchers.IO) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
            bitmap.recycle()
        }
        return file
    }

    private fun downScaleBitmap(bitmap: Bitmap, factor: Float) = Bitmap.createScaledBitmap(
        bitmap, (factor * bitmap.width).toInt(), (factor * bitmap.height).toInt(), true
    )

    private fun calculateInSampleSize(
        options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int,
    ): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        Log.d("ImageFileHelper", "height: $height, width: $width")

        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2

                Log.d("ImageFileHelper", "inSampleSize: $inSampleSize")
            }
        }

        return inSampleSize
    }

    fun decodeSampledBitmap(file: File, reqWidth: Int, reqHeight: Int): Bitmap {
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(file.path, this)

            inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)

            inJustDecodeBounds = false

            BitmapFactory.decodeFile(file.path, this)
        }
    }

    fun createCustomTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(getTimestamp(), ".jpg", storageDir)
    }

    fun fixImageOrientation(file: File) {
        val ei = ExifInterface(file.path)
        val orientation: Int = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        val matrix = Matrix()

        val bitmap = BitmapFactory.decodeFile(file.path)
        val degrees = when (orientation) {
            // @formatter:off
            ExifInterface.ORIENTATION_ROTATE_90  -> { matrix.postRotate(90f);  90f  }
            ExifInterface.ORIENTATION_ROTATE_180 -> { matrix.postRotate(180f); 180f }
            ExifInterface.ORIENTATION_ROTATE_270 -> { matrix.postRotate(270f); 270f }
            ExifInterface.ORIENTATION_NORMAL     -> { 0f }
            else                                 -> { 0f }
            // @formatter:on
        }

        val flip = when (orientation) {
            // @formatter:off
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> { matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f); "h" }
            ExifInterface.ORIENTATION_FLIP_VERTICAL   -> { matrix.postScale(1f, -1f, bitmap.width / 2f, bitmap.height / 2f); "v" }
            else                                      -> { "n" }
            // @formatter:on
        }
        Log.d("ImageFileHelper", "fixImageRotation, orientation: $flip|$degrees")

        val rotatedBitmap = Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
        )

        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(file))
    }

    fun uriToFile(imageUri: Uri, context: Context): File {
        val contentResolver = context.contentResolver
        val myFile = createCustomTempFile(context)

        val inputStream = contentResolver.openInputStream(imageUri) as InputStream
        val outputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int

        while (inputStream.read(buf)
                    .also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }
}