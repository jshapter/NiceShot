package com.example.niceshot.viewmodels

import androidx.lifecycle.ViewModel
import com.example.niceshot.data.DataRepository
import com.example.niceshot.data.entities.Photo
import com.example.niceshot.data.entities.User

class ViewProfileViewModel (
    photoRepository: DataRepository,
) : ViewModel() {

    private val rep = photoRepository

    fun returnUser (
        selectedId: Int,
        photoRepository: DataRepository = rep
    ) : User {
        return photoRepository.getUserStream(selectedId)
    }

    fun userPhotos(
        selectedId: Int,
        photoRepository: DataRepository = rep
    ) : List<Photo> {
        return photoRepository.getPhotosByUser(selectedId)
    }

}