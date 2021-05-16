package com.example.youinvited.ui.mapEdit

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

class MapView(context: Context): View(context) {
    var paint:Paint? = null
    var posX:Float = 0.0f
    var posY:Float = 0.0f


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        this.paint = Paint()
        this.paint?.isFilterBitmap = true
        this.paint?.isAntiAlias = true
        this.paint?.color = Color.BLUE
        canvas?.drawColor(Color.WHITE)
        //canvas?.drawCircle(50f, 50f, 50f, paint!!)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        return super.onTouchEvent(event)
    }
}