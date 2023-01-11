package com.albatta.mohmmed.mishal.onbording

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

object Utils {
    fun getBytes(bitmap: Bitmap):ByteArray{
        val stream= ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream)
        return stream.toByteArray()
    }
    fun getImage(image: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(image,0,image.size)
    }
}