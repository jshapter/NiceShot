package com.example.niceshot.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 0,
    val firstName: String,
    val secondName: String,
    val email: String,
    val password: String,
    val created: String,
//    val posts: Int,

    val profilePictureUri: String
)
