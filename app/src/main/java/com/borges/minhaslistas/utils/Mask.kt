package com.borges.minhaslistas.utils

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import java.text.NumberFormat
import java.util.*

class Mask {
    companion object {
        fun real(editText: EditText): TextWatcher {

            var current: String = ""
            return object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                }
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                }
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                    if (s.toString() != current) {
                        Log.d("MASK", s.toString())

                        editText.removeTextChangedListener(this)

                        val cleanString: String = s.replace("""[R$ 0,.]""".toRegex(), "").trim()

                        if (s.isEmpty()) {
                            editText.setText("")
                            editText.setSelection("".length)
                            editText.addTextChangedListener(this)
                        }
                        val parsed = cleanString.toDouble()
                        val meuLocal = Locale("pt", "BR")
                        val formatted = NumberFormat.getCurrencyInstance(meuLocal).format((parsed / 100))

                        current = formatted
                        editText.setText(formatted)
                        editText.setSelection(formatted.length)

                        editText.addTextChangedListener(this)
                    }
                }
            }
        }
    }
}