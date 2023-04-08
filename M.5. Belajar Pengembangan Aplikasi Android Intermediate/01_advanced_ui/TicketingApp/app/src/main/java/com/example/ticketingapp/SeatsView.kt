package com.example.ticketingapp

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.View
import androidx.core.content.res.ResourcesCompat

class SeatsView : View {
    var seat: Seat? = null

    private val backgroundPaint = Paint()
    private val armrestPaint = Paint()
    private val bottomSeatPaint = Paint()
    private val bounds = Rect()
    private val numberSeatPaint = Paint(Paint.FAKE_BOLD_TEXT_FLAG)
    private val titlePaint = Paint(Paint.FAKE_BOLD_TEXT_FLAG)

    // @formatter:off
    constructor(context: Context) :super(context)
    constructor(context: Context, attrs: AttributeSet) :super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :super(context, attrs, defStyleAttr)
    // @formatter:on

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val height = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)

        val halfOfHeight = height / 2.0f
        val halfOfWidth = width / 2.0f

        var value = -600.0f

        for (i in 0 until seats.size) {
            if (i % 2 == 0) {
                seats[i].apply {
                    val _offset = -300.0f
                    x = halfOfWidth + _offset
                    y = halfOfHeight + value
                }
            } else {
                seats[i].apply {
                    val _offset = 100.0f
                    x = halfOfWidth + _offset
                    y = halfOfHeight + value
                }
                value += 300.0f
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        for (seat in seats)
            drawSeat(canvas, seat)

        val text = "Silakan pilih kursi"
        titlePaint.textSize = 50.0f

        canvas?.drawText(text, (width / 2f) - 197f, 100f, titlePaint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val halfOfHeight = height / 2
        val halfOfWidth = width / 2

        val widthColumnOne = (halfOfWidth - 300f)..(halfOfWidth - 100f)
        val widthColumnTwo = (halfOfWidth + 100f)..(halfOfWidth + 300f)

        val heightRowOne = (halfOfHeight - 600F)..(halfOfHeight - 400F)
        val heightRowTwo = (halfOfHeight - 300F)..(halfOfHeight - 100F)
        val heightRowTree = (halfOfHeight + 0F)..(halfOfHeight + 200F)
        val heightRowFour =(halfOfHeight + 300F)..(halfOfHeight + 500F)

        if (event?.action == ACTION_DOWN) {
            when {
                event.x in widthColumnOne && event.y in heightRowOne -> booking(0)
                event.x in widthColumnTwo && event.y in heightRowOne -> booking(1)
                event.x in widthColumnOne && event.y in heightRowTwo -> booking(2)
                event.x in widthColumnTwo && event.y in heightRowTwo -> booking(3)
                event.x in widthColumnOne && event.y in heightRowTree -> booking(4)
                event.x in widthColumnTwo && event.y in heightRowTree -> booking(5)
                event.x in widthColumnOne && event.y in heightRowFour -> booking(6)
                event.x in widthColumnTwo && event.y in heightRowFour -> booking(7)
            }
        }
        return true
    }

    private fun booking(position: Int) {
        for (seat in seats)
            seat.isBooked = false
        seats[position].apply {
            seat = this
            isBooked = true
        }
        invalidate()
    }

    private fun drawSeat(canvas: Canvas?, seat: Seat) {
        if (seat.isBooked) {
            backgroundPaint.color = ResourcesCompat.getColor(resources, R.color.grey_200, null)
            armrestPaint.color = ResourcesCompat.getColor(resources, R.color.grey_200, null)
            bottomSeatPaint.color = ResourcesCompat.getColor(resources, R.color.grey_200, null)
            numberSeatPaint.color = ResourcesCompat.getColor(resources, R.color.black, null)
        } else {
            backgroundPaint.color = ResourcesCompat.getColor(resources, R.color.blue_500, null)
            armrestPaint.color = ResourcesCompat.getColor(resources, R.color.blue_700, null)
            bottomSeatPaint.color = ResourcesCompat.getColor(resources, R.color.blue_200, null)
            numberSeatPaint.color = ResourcesCompat.getColor(resources, R.color.grey_200, null)
        }

        // menyimpan state
        canvas?.save()

        // background
        canvas?.translate(seat.x!!, seat.y!!)

        val backgroundPath = Path().apply {
            addRect(0F, 0F, 200F, 200F, Path.Direction.CCW)
            addCircle(100F, 50F, 75F, Path.Direction.CCW)
            canvas?.drawPath(this, backgroundPaint)
        }

        // Sandaran Tangan
        val armrestPath = Path().apply {
            addRect(0F, 0F, 50F, 200F, Path.Direction.CCW)
            canvas?.drawPath(this, armrestPaint)
            canvas?.translate(150F, 0F)

            addRect(0F, 0F, 50F, 200F, Path.Direction.CCW)
            canvas?.drawPath(this, armrestPaint)
        }

        // Bagian Bawah Kursi
        canvas?.translate(-150F, 175F)
        val bottomSeatPath = Path().apply {
            addRect(0F, 0F, 200F, 25F, Path.Direction.CCW)
            canvas?.drawPath(this, bottomSeatPaint)
        }

        // Menulis Nomor Kursi
        canvas?.translate(0F, -175F)
        numberSeatPaint.apply {
            textSize = 50F
            numberSeatPaint.getTextBounds(seat.name, 0, seat.name.length, bounds)
        }
        canvas?.drawText(seat.name, 100F - bounds.centerX(), 100F, numberSeatPaint)

        // Mengembalikan ke pengaturan sebelumnya
        canvas?.restore()
    }

    companion object {
        private val seats: ArrayList<Seat> = arrayListOf(
            Seat(id = 1, name = "A1", isBooked = false),
            Seat(id = 2, name = "A2", isBooked = false),
            Seat(id = 3, name = "B1", isBooked = false),
            Seat(id = 4, name = "B2", isBooked = false),
            Seat(id = 5, name = "C1", isBooked = false),
            Seat(id = 6, name = "C2", isBooked = false),
            Seat(id = 7, name = "D2", isBooked = false),
            Seat(id = 8, name = "D3", isBooked = false),
        )
    }
}