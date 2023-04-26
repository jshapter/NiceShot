package com.example.niceshot.data

import com.example.niceshot.data.entities.Photo
import com.example.niceshot.data.entities.User
import kotlinx.coroutines.flow.Flow

class OfflineDataRepository(private val dataDao: DataDao) : DataRepository {

    override fun getAllUsersStream(): Flow<List<User>> = dataDao.getAllUsers()

    override fun getUserStream(id: Int): User = dataDao.getUser(id)

    override fun getUserByEmail(email: String): User = dataDao.getUserByEmail(email)

    override suspend fun insertUser(User: User) = dataDao.insertUser(User)

    override suspend fun deleteUser(User: User) = dataDao.deleteUser(User)

    override suspend fun updateUser(
        firstName: String,
        secondName: String,
        email: String,
        password: String,
        profilePictureUri: String,
        id: Int
    ) = dataDao.updateUser(firstName, secondName, email, password, profilePictureUri, id)
//    override suspend fun updateProfile(firstName: String, id: Int?) = photoDao.updateProfile(User)


    override fun getAllPhotosStream(): Flow<List<Photo>> = dataDao.getAllPhotos()

    override fun getPhotoStream(id: Int): Flow<Photo?> = dataDao.getPhoto(id)
    override fun getPhoto(photoId: Int): Photo? {
        TODO("Not yet implemented")
    }

    override suspend fun insertPhoto(Photo: Photo) = dataDao.insert(Photo)

    override suspend fun deletePhoto(Photo: Photo) = dataDao.delete(Photo)

    override suspend fun updatePhoto(Photo: Photo) = dataDao.update(Photo)


    override fun getPhotosByUser(userId: Int): List<Photo> = dataDao.getPhotosByUser(userId)
}