package com.example.mystoryapp.ui.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.example.mystoryapp.R

class EmailInputView : AppCompatEditText {
    // @formatter:off
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)
    // @formatter:on

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(s ?: "").matches())
                    error = context.getString(R.string.ed_login_email_error)
                else
                    error = null
            }

            override fun afterTextChanged(s: Editable?) {
                // do nothing
            }

        })
    }
}