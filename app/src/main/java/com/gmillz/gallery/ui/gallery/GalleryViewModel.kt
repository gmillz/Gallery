package com.gmillz.gallery.ui.gallery

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.gmillz.gallery.data.Photo
import com.gmillz.gallery.util.PhotoUtils
import java.time.LocalDate
import java.util.SortedMap


class GalleryViewModel(application: Application): AndroidViewModel(application) {
    val photos: MutableState<SortedMap<String, ArrayList<Photo>>> = mutableStateOf(sortedMapOf())

    init {
        val hashMap = hashMapOf<String, ArrayList<Photo>>()
        val photos = PhotoUtils.getPhotos(getApplication(), "")
        for (photo in photos) {
            if (!hashMap.containsKey(photo.date.toLocalDate().toString())) {
                hashMap[photo.date.toLocalDate().toString()] = arrayListOf()
            }
            hashMap[photo.date.toLocalDate().toString()]!!.add(photo)
            this.photos.value = hashMap.toSortedMap(Comparator { key1, key2 ->
                LocalDate.parse(key1).toEpochDay().compareTo(LocalDate.parse(key2).toEpochDay())
            })
        }
    }
}