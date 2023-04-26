package com.example.niceshot.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.niceshot.data.entities.Photo
import com.example.niceshot.data.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
interface DataDao {

    // User actions
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

//    @Update
//    suspend fun updateUser(firstName: String)

    @Query("UPDATE users SET firstName = :firstName, secondName = :secondName, email = :email, password = :password, profilePictureUri = :profilePictureUri WHERE userId = :id")
    suspend fun updateUser(
        firstName: String,
        secondName: String,
        email: String,
        password: String,
        profilePictureUri: String,
        id: Int
    )

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * from users ORDER BY userId ASC")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * from users WHERE userId = :userId")
    fun getUser(userId: Int): User

    @Query("SELECT * from users WHERE email = :email")
    fun getUserByEmail(email: String): User


    // Photo actions
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(photo: Photo)

    @Update
    suspend fun update(photo: Photo)

    @Delete
    suspend fun delete(photo: Photo)

    @Query("SELECT * from photos ORDER BY photoId DESC")
    fun getAllPhotos(): Flow<List<Photo>>

    @Query("SELECT * from photos WHERE photoId = :photoId")
    fun getPhoto(photoId: Int): Flow<Photo>


    // Joined actions
//    @Transaction
//    @Query("SELECT * FROM users WHERE userId = :userId")
//    suspend fun getUserWithPhotos(userId: Int): List<UserWithPhotos>

    @Transaction
    @Query("SELECT * FROM photos WHERE creatorId = :userId")
    fun getPhotosByUser(userId: Int): List<Photo>
}