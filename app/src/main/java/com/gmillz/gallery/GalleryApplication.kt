package com.gmillz.gallery

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gmillz.gallery.data.Photo
import java.util.SortedMap

class GalleryApplication: Application() {

    private val _photos = MutableLiveData<HashMap<String, ArrayList<Photo>>>()
    val photos: LiveData<HashMap<String, ArrayList<Photo>>>
    get() = _photos
}