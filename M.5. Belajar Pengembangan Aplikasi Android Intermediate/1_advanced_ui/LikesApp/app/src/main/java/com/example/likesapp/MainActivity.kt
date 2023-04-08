package com.example.likesapp

import android.graphics.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import com.example.likesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888)
    private val canvas = Canvas(bitmap)
    private val paint = Paint()

    private val left = 150f
    private val top = 250f
    private val right = bitmap.width - left
    private val bottom = bitmap.height - 50f

    private val halfOfWidth = bitmap.width / 2f
    private val halfOfHeight = bitmap.height / 2f

    private val message = "Apakah kamu suka bermain?"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.imageView.setImageBitmap(bitmap)

        showText()

        binding.like.setOnClickListener {
            showEars()
            showFace()
            showMouth(true)
            showEyes()
            showNose()
            showHair()
        }

        binding.dislike.setOnClickListener {
            showEars()
            showFace()
            showMouth(false)
            showEyes()
            showNose()
            showHair()
        }

        // menghapus object dalam canvas
//        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC)
    }

    private fun showFace() {
        val face = RectF(left, top, right, bottom)

        paint.color = ResourcesCompat.getColor(resources, R.color.yellow_left_skin, null)
        canvas.drawArc(face, 90f, 180f, false, paint)

        paint.color = ResourcesCompat.getColor(resources, R.color.yellow_right_skin, null)
        canvas.drawArc(face, 270f, 180f, false, paint)
    }

    private fun showEyes() {
        paint.color = ResourcesCompat.getColor(resources, R.color.black, null)
        canvas.drawCircle(halfOfWidth - 100f, halfOfHeight - 10f, 50f, paint)
        canvas.drawCircle(halfOfWidth + 100f, halfOfHeight - 10f, 50f, paint)

        paint.color = ResourcesCompat.getColor(resources, R.color.white, null)
        canvas.drawCircle(halfOfWidth - 120f, halfOfHeight - 20f, 15f, paint)
        canvas.drawCircle(halfOfWidth + 80f, halfOfHeight - 20f, 15f, paint)
    }

    private fun showMouth(isHappy: Boolean) {
        when (isHappy) {
            true -> {
                // @formatter:off
                paint.color = ResourcesCompat.getColor(resources, R.color.black, null)
                val lip = RectF(halfOfWidth - 200f, halfOfHeight - 100f, halfOfWidth + 200f, halfOfHeight + 400f)
                canvas.drawArc(lip, 25f, 130f, false, paint)

                paint.color = ResourcesCompat.getColor(resources, R.color.white, null)
                val mount = RectF(halfOfWidth - 180f, halfOfHeight, halfOfWidth + 180f, halfOfHeight + 380f)
                canvas.drawArc(mount, 25f, 130f, false, paint)
                // @formatter:on
            }
            false -> {
                // @formatter:off
                paint.color = ResourcesCompat.getColor(resources, R.color.black, null)
                val lip = RectF(halfOfWidth - 200f, halfOfHeight + 250f, halfOfWidth + 200f, halfOfHeight + 350f)
                canvas.drawArc(lip, 0f, -180f, false, paint)

                paint.color = ResourcesCompat.getColor(resources, R.color.white, null)
                val mouth = RectF(halfOfWidth - 180f, halfOfHeight + 260f, halfOfWidth + 180f, halfOfHeight + 330f)
                canvas.drawArc(mouth, 0f, -180f, false, paint)
                // @formatter:on
            }
        }
    }

    private fun showNose() {
        paint.color = ResourcesCompat.getColor(resources, R.color.black, null)
        canvas.drawCircle(halfOfWidth - 40f, halfOfHeight + 140f, 15f, paint)
        canvas.drawCircle(halfOfWidth + 40f, halfOfHeight + 140f, 15f, paint)
    }

    private fun showEars() {
        paint.color = ResourcesCompat.getColor(resources, R.color.brown_left_hair, null)
        canvas.drawCircle(halfOfWidth - 300F, halfOfHeight - 100F, 100F, paint)

        paint.color = ResourcesCompat.getColor(resources, R.color.brown_right_hair, null)
        canvas.drawCircle(halfOfWidth + 300F, halfOfHeight - 100F, 100F, paint)

        paint.color = ResourcesCompat.getColor(resources, R.color.red_ear, null)
        canvas.drawCircle(halfOfWidth - 300F, halfOfHeight - 100F, 60F, paint)
        canvas.drawCircle(halfOfWidth + 300F, halfOfHeight - 100F, 60F, paint)
    }

    private fun showHair() {
        canvas.save()

        val path = Path().apply {
            addCircle(halfOfWidth - 100f, halfOfHeight - 10f, 150f, Path.Direction.CCW)
            addCircle(halfOfWidth + 100f, halfOfHeight - 10f, 150f, Path.Direction.CCW)
        }

        val mouth = RectF(halfOfWidth - 250f, halfOfHeight, halfOfWidth + 250f, halfOfHeight + 500f)
        path.addOval(mouth, Path.Direction.CCW)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            canvas.clipPath(path, Region.Op.DIFFERENCE)
        else
            canvas.clipOutPath(path)

        val face = RectF(left, top, right, bottom)

        paint.color = ResourcesCompat.getColor(resources, R.color.brown_left_hair, null)
        canvas.drawArc(face, 90f, 180f, false, paint)

        paint.color = ResourcesCompat.getColor(resources, R.color.brown_right_hair, null)
        canvas.drawArc(face, 270f, 180f, false, paint)

        canvas.restore()
    }

    private fun showText() {
        val paintText = Paint(Paint.FAKE_BOLD_TEXT_FLAG).apply {
            textSize = 50f
            color = ResourcesCompat.getColor(resources, R.color.black, null)
        }

        val bounds = Rect()
        paintText.getTextBounds(message, 0, message.length, bounds)

        val x = halfOfWidth - bounds.centerX()
        val y = 50f
        canvas.drawText(message, x, y, paintText)
    }
}