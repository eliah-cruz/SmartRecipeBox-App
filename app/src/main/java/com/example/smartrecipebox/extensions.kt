package com.example.smartrecipebox

import android.graphics.LinearGradient
import android.graphics.Shader
import android.widget.TextView
import androidx.core.content.ContextCompat

fun TextView.applyTextGradient(colorStart: Int, colorEnd: Int) {
    // Prevent re-applying if shader already exists to stop flickering
    if (this.paint.shader != null) return

    this.post {
        val paint = this.paint
        val width = paint.measureText(this.text.toString())
        if (width > 0f) {
            val textShader = LinearGradient(
                0f, 0f, width, this.textSize,
                ContextCompat.getColor(this.context, colorStart),
                ContextCompat.getColor(this.context, colorEnd),
                Shader.TileMode.CLAMP
            )
            this.paint.shader = textShader
            this.invalidate()
        }
    }
}