package com.gmillz.gallery.util

import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.gmillz.gallery.data.Album
import com.gmillz.gallery.data.Photo
import com.gmillz.gallery.data.Video
import java.time.Instant
import java.time.ZoneId
import java.util.Date

object PhotoUtils {
    fun getAlbums(context: Context): ArrayList<Album> {
        val albums = arrayListOf<Album>()
        val paths = arrayListOf<String>()
        val projection = arrayOf(
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_ID)
        context.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
            null, null, null).use { cursor ->
            if (cursor?.count == 0) return@use
            cursor?.moveToFirst()?: return@use

            do {
                //val name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                val folder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                val dataPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))

                if (dataPath.isEmpty() || folder.isNullOrEmpty()) continue
                val folderPaths = dataPath.substring(0, dataPath.lastIndexOf("$folder/")) + folder + "/"
                if (!paths.contains(folderPaths)) {
                    paths.add(folderPaths)

                    val album = Album(folderPaths, folder, 1, dataPath)
                    albums.add(album)
                } else {
                    if (paths.contains(folderPaths)) {
                        val index = paths.indexOf(folderPaths)
                        albums[index].firstPic = dataPath
                        albums[index].picsCount++
                    }
                }
            } while (cursor.moveToNext())
        }
        albums.sortBy { it.name }
        return albums
    }

    fun getPhotos(context: Context, album: String = ""): ArrayList<Photo> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (context.checkSelfPermission(android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                return arrayListOf()
            }
        } else {
            if (context.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                return arrayListOf()
            }
        }
        Log.d("TEST", "getAllPhotos")
        val photos = arrayListOf<Photo>()
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.ImageColumns.DATE_MODIFIED
        )

        val selection = if (album.isNotEmpty()) MediaStore.Images.Media.DATA + " like ? " else null
        val selectionArgs = if (album.isNotEmpty()) arrayOf("%$album%") else null
        context.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
            selection, selectionArgs, null).use { cursor ->
            if (cursor?.count == 0) return@use
            cursor?.moveToFirst()?: return@use

            do {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA))
                val size = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE))
                val date = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_MODIFIED))

                val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                Log.d("TEST", "date - $date")

                Log.d("TEST", "date - ${Date(date * 1000)}")

                val photo = Photo(name, uri, size, Instant.ofEpochSecond(date).atZone(ZoneId.systemDefault()).toLocalDateTime())
                photos.add(photo)
            } while (cursor.moveToNext())
        }
        photos.sortBy { it.date }
        photos.reverse()
        return photos
    }

    fun getVideos(context: Context): ArrayList<Video> {
        val videos = arrayListOf<Video>()
        val projection = arrayOf(
            MediaStore.Video.VideoColumns.DATA,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.VideoColumns.DATE_MODIFIED,
        )

        context.contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection,
            null, null, null).use { cursor ->
            if (cursor?.count == 0) return@use
            cursor?.moveToFirst()?: return@use

            do {
                val name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME))
                val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DATA))
                val size = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE))
                val date = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_MODIFIED))

                val video = Video(name, path, size, date)
                videos.add(video)
            } while (cursor.moveToNext())
        }
        videos.sortBy { it.date }
        videos.reverse()
        return videos
    }

}