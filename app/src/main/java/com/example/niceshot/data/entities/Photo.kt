package com.example.niceshot.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class Photo(
    @PrimaryKey(autoGenerate = true)
    val photoId: Int = 0,
    val uri: String,
    val makeModel: String,
    val dateTime: String,
    val creatorId: Int,
    val location: String,
    val caption: String,
    val aperture: String,
    val shutterSpeed: String,
    val dateUploaded: String,
//    val gps: String,
)
