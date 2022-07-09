package com.samuelnunes.utility_tool_kit.debounce

import android.text.Editable
import android.text.TextWatcher
import java.util.*


abstract class DebounceTextWatcher(private val timeDelayed: Long = 300L) : TextWatcher {

    private var timer: Timer? = null
    override fun afterTextChanged(editable: Editable) {
        timer = Timer()
        val text = editable.toString()
        timer!!.schedule(object : TimerTask() {
            override fun run() = runAfterDelayed(text)
        }, timeDelayed)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        timer?.cancel()
    }

    abstract fun runAfterDelayed(query: String)
}