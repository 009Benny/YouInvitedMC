package com.example.youinvited.ui.mapEdit

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.youinvited.models.InvitedClass

interface MapEditDelegate{
    fun saveChanges()
}

class MapView: View {
    var invited:InvitedClass? = null
    var posX:Float = 0.0f
    var posY:Float = 0.0f
    var delegate:MapEditDelegate? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val paint = Paint()
        paint.isFilterBitmap = true
        paint.isAntiAlias = true
        paint.color = Color.BLUE
        if (this.invited?.x != null && this.invited?.y != null){
            canvas?.drawCircle(this.invited!!.x!!.toFloat(), this.invited!!.y!!.toFloat(), 10.0f, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (this.invited != null){
            this.invited?.x = event?.x?.toDouble() ?: 0.0
            this.invited?.y = event?.y?.toDouble() ?: 0.0
        }
        invalidate()
        return super.onTouchEvent(event)
    }

    fun showInvited(invited:InvitedClass){
        this.invited = invited
        invalidate()
    }

}