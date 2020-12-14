package com.pavesid.androidacademy.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

object CompressBitmap {

    fun compressToString(bmp: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT)
    }

    fun uncompressFromString(encoded: String): Bitmap {
        val bytes = Base64.decode(encoded.toByteArray(), Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}
