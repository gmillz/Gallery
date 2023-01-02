package com.gmillz.gallery.data

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Size
import java.io.File

class Video(val name: String, val path: String, val size: String, val date: Long = 0L) {
    fun getThumbnail(context: Context): Bitmap {
        return context.contentResolver.loadThumbnail(Uri.fromFile(File(path)), Size(120, 120), null)
    }
}