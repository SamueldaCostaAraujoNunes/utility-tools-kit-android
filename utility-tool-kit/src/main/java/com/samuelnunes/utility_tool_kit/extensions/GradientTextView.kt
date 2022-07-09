package com.samuelnunes.utility_tool_kit.extensions

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Rect
import android.graphics.Shader
import android.widget.TextView
import com.samuelnunes.utility_tool_kit.R
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

fun TextView.setGradientTextColor(
    angle: Float,
    colors: IntArray,
    positions: FloatArray? = null
) {
    post {
        val textBound = Rect(
            Int.MAX_VALUE,
            Int.MAX_VALUE,
            Int.MIN_VALUE,
            Int.MIN_VALUE
        )
        for (i in 0 until lineCount) {
            val left = layout.getLineLeft(i)
            val right = layout.getLineRight(i)
            if (left < textBound.left) textBound.left = left.toInt()
            if (right > textBound.right) textBound.right = right.toInt()
        }
        textBound.top = layout.getLineTop(0)
        textBound.bottom = layout.getLineBottom(lineCount - 1)
        if (includeFontPadding) {
            val fontMetrics = paint.fontMetrics
            textBound.top += (fontMetrics.ascent - fontMetrics.top).toInt()
            textBound.bottom -= (fontMetrics.bottom - fontMetrics.descent).toInt()
        }
        val angleInRadians = Math.toRadians(angle.toDouble())
        val r = sqrt(
            (textBound.bottom - textBound.top).toDouble().pow(2.0) +
                    (textBound.right - textBound.left).toDouble().pow(2.0)
        ) / 2
        val centerX = (textBound.left + (textBound.right - textBound.left) / 2).toFloat()
        val centerY = (textBound.top + (textBound.bottom - textBound.top) / 2).toFloat()
        val startX = textBound.left.toDouble().coerceAtLeast(
            textBound.right.toDouble().coerceAtMost(centerX - r * cos(angleInRadians))
        ).toFloat()
        val startY = textBound.bottom.toDouble().coerceAtMost(
            textBound.top.toDouble().coerceAtLeast(centerY - r * sin(angleInRadians))
        ).toFloat()
        val endX = textBound.left.toDouble().coerceAtLeast(
            textBound.right.toDouble().coerceAtMost(centerX + r * cos(angleInRadians))
        ).toFloat()
        val endY = textBound.bottom.toDouble().coerceAtMost(
            textBound.top.toDouble().coerceAtLeast(centerY + r * sin(angleInRadians))
        ).toFloat()
        val textShader: Shader = LinearGradient(
            startX, startY, endX, endY,
            colors, positions,
            Shader.TileMode.CLAMP
        )
        setTextColor(Color.WHITE)
        paint.shader = textShader
    }
}
