package com.example.niceshot.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.niceshot.data.entities.Photo
import com.example.niceshot.data.entities.User

data class UserWithPhotos (
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "creatorId"
    )
    val photos: List<Photo>
    )