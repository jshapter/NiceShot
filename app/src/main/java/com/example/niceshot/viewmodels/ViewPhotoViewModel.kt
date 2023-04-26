package com.example.niceshot.viewmodels

import androidx.lifecycle.ViewModel
import com.example.niceshot.data.DataRepository
import com.example.niceshot.data.entities.Photo
import kotlinx.coroutines.flow.Flow

class ViewPhotoViewModel(private val dataRepository: DataRepository) : ViewModel() {


    fun getPhoto(photoId: Int) : Photo? {
        return dataRepository.getPhoto(photoId)
    }



}