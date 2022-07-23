package Utils

import androidx.core.content.ContextCompat
import android.graphics.Bitmap
import android.content.Context
import android.graphics.Canvas
import java.util.*

object CreateBitmap {
    fun createBitmap(context: Context?, drawableId: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(context!!, drawableId)
        val bitmap = Objects.requireNonNull(drawable).let {
            it?.let { it1 ->
                Bitmap.createBitmap(it1.intrinsicWidth,
                        drawable!!.intrinsicHeight, Bitmap.Config.ARGB_8888)
            }
        }
        val canvas = bitmap?.let { Canvas(it) }
        canvas?.let { drawable?.setBounds(0, 0, it.width, canvas.height) }
        if (canvas != null) {
            drawable?.draw(canvas)
        }
        return bitmap
    }
}