package com.example.niceshot.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.niceshot.data.DataRepository
import com.example.niceshot.data.entities.User

class EditProfileViewModel (
    private val dataRepository: DataRepository
) : ViewModel() {

    var editProfileUiState by mutableStateOf(UserUiState())

    private val rep = dataRepository

    suspend fun deleteUser (
        selectedId: Int,
    ) {
        val user = returnUser(selectedId)
        dataRepository.deleteUser(user)
    }

    fun returnUser (
        selectedId: Int,
        photoRepository: DataRepository = rep
    ) : User {
        return photoRepository.getUserStream(selectedId)
    }

    suspend fun updateAccount(
        firstName: String,
        secondName: String,
        email: String,
        password: String,
        profilePictureUri: String,
        id: Int
    ) {
        dataRepository.updateUser(
            firstName,
            secondName,
            email,
            password,
            profilePictureUri,
            id
        )
    }
}