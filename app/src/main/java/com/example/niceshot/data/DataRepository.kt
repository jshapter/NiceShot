package com.example.niceshot.data

import com.example.niceshot.data.entities.Photo
import com.example.niceshot.data.entities.User
import kotlinx.coroutines.flow.Flow

interface DataRepository {
    fun getAllUsersStream(): Flow<List<User>>

    fun getUserStream(userId: Int): User

    fun getUserByEmail(email: String): User

    suspend fun insertUser(User: User)

    suspend fun deleteUser(User: User)

    suspend fun updateUser(
        firstName: String,
        secondName: String,
        email: String,
        password: String,
        profilePictureUri: String,
        id: Int
    )

    fun getAllPhotosStream(): Flow<List<Photo>>

    fun getPhotoStream(photoId: Int): Flow<Photo?>

    fun getPhoto(photoId: Int): Photo?

    suspend fun insertPhoto(Photo: Photo)

    suspend fun deletePhoto(Photo: Photo)

    suspend fun updatePhoto(Photo: Photo)

    fun getPhotosByUser(userId: Int): List<Photo>
}