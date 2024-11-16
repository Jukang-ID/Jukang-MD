package com.example.jukang.helper.costum

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText

class editnumberfield @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateNumber(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun validateNumber(number: String) {
        if (number.isNotEmpty() && !Patterns.PHONE.matcher(number).matches()) {
            error = "Format nomor tidak valid"
        } else {
            error = null
        }
    }
}