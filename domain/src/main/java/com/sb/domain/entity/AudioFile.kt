package com.sb.domain.entity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore

data class AudioFile(
    val uri: Uri,
    val title: String,
    val artist: String,
    val albumArt: Bitmap?,
    val duration: Long,
) {
    companion object {
        fun parseFromUri(context: Context, uri: Uri): AudioFile? {
            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.BOOKMARK,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DURATION,
            )
            val cursor = context.contentResolver.query(
                uri,
                projection,
                null,
                null,
                null
            )
            return cursor?.use {
                if (it.moveToFirst()) {
                    val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                    val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                    val albumIdColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
                    val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

                    val title = it.getString(titleColumn)
                    val artist = it.getString(artistColumn)
                    val albumId = it.getLong(albumIdColumn)
                    val duration = it.getLong(durationColumn)

                    val albumArt = getAlbumArt(context, albumId)
                    AudioFile(uri, title, artist, albumArt, duration)
                } else null
            }
        }

        private fun getAlbumArt(context: Context, albumId: Long): Bitmap? {
            val albumArtUri = Uri.parse("content://media/external/audio/albumart/$albumId")
            try {
                val inputStream = context.contentResolver.openInputStream(albumArtUri)
                return BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }
    }


}