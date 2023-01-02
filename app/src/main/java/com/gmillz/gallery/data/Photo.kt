package com.gmillz.gallery.data

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Size
import java.time.LocalDateTime

class Photo(val name: String, val uri: Uri, val size: String, val date: LocalDateTime) {
    fun getThumbnail(context: Context): Bitmap {
        return context.contentResolver.loadThumbnail(uri, Size(120, 120), null)
    }
}