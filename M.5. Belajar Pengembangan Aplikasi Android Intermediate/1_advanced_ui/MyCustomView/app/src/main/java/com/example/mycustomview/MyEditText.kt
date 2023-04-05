package com.example.mycustomview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintSet.Motion
import androidx.core.content.ContextCompat

class MyEditText : AppCompatEditText, View.OnTouchListener {
    private var clearButtonImage: Drawable

    // @formatter:off
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    // @formatter:on

    init {
        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_close_black_24dp)!!
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) showClearButton() else hideClearButton()
            }

            override fun afterTextChanged(s: Editable?) {
                // do nothing
            }
        })
    }

    override fun onTouch(view: View?, event: MotionEvent): Boolean {
        val (start, top, end, bottom) = IntArray(4) { it }
        if (compoundDrawables[end] == null)
            return false

        val isClearButtonClicked = if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
            val clearButtonEnd = (clearButtonImage.intrinsicWidth + paddingStart).toFloat()
            when {
                event.x < clearButtonEnd -> true
                else -> false
            }
        } else {
            val clearButtonStart = (width - paddingEnd - clearButtonImage.intrinsicWidth).toFloat()
            when {
                event.x > clearButtonStart -> true
                else -> false
            }
        }

        if (!isClearButtonClicked)
            return false

        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                clearButtonImage = ContextCompat.getDrawable(
                    context, R.drawable.ic_close_black_24dp
                )!!
                showClearButton()
                true
            }
            MotionEvent.ACTION_UP -> {
                clearButtonImage = ContextCompat.getDrawable(
                    context, R.drawable.ic_close_black_24dp
                )!!
                text?.clear()
                hideClearButton()
                true
            }
            else -> false
        }
    }

    private fun showClearButton() {
        setButtonDrawables(endOfTheText = clearButtonImage)
    }

    private fun hideClearButton() {
        setButtonDrawables()
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null,
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText, topOfTheText, endOfTheText, bottomOfTheText
        )
    }
}