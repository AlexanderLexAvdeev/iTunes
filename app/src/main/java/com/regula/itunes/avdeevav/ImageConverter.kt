package com.regula.itunes.avdeevav

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import android.widget.ImageView

import java.io.ByteArrayOutputStream
import android.graphics.ColorMatrixColorFilter
import android.graphics.ColorMatrix
import android.graphics.Bitmap


object ImageConverter {

    fun getGrayScaleString(imageView: ImageView): String? {

        // to gray scale
        val drawable: Drawable = imageView.drawable
        val bitmap: Bitmap = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val grayScale: Bitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        val canvas = Canvas(grayScale)
        val matrix = ColorMatrix()
        matrix.setSaturation(0f)
        val filter = ColorMatrixColorFilter(matrix)
        val paint = Paint()
        paint.colorFilter = filter
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        // to byte array
        val stream = ByteArrayOutputStream()
        grayScale.compress(Bitmap.CompressFormat.WEBP, 100, stream)
        val array: ByteArray = stream.toByteArray()

        // to string
        return Base64.encodeToString(array, Base64.DEFAULT)
    }

    fun getBitmap(imageString: String?): Bitmap? {

        val string: String = imageString ?: ""
        val array: ByteArray = Base64.decode(string, Base64.DEFAULT)

        return BitmapFactory.decodeByteArray(array, 0, array.size)
    }
}
