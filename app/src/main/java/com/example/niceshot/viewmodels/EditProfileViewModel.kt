package com.example.niceshot.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.niceshot.data.DataRepository
import com.example.niceshot.data.entities.Photo
import com.example.niceshot.data.entities.User

class EditProfileViewModel (private val dataRepository: DataRepository) : ViewModel() {

    var editProfileUiState by mutableStateOf(UserUiState())

    private val rep = dataRepository

    fun userPhotos(
        selectedId: Int,
        photoRepository: DataRepository = rep
    ) : List<Photo> {
        return photoRepository.getPhotosByUser(selectedId)
    }

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
        //val updatedUser = userUiState.userDetails.toUser()
        dataRepository.updateUser(
            firstName,
            secondName,
            email,
            password,
            profilePictureUri,
            id
        )
        // viewModel.createProfileUiState.userDetails.toUser()
    }

}

data class UserUiState2(
    val userDetails: UserDetails = UserDetails()
)

//data class UserDetails(
//    var firstName: String = "",
//    var secondName: String = "",
//    var email: String = "",
//    var password: String = "",
//    var profilePictureUri: String = ""
//)
//
//fun UserDetails.toUser(): User = User(
//    firstName = firstName,
//    secondName = secondName,
//    email = email,
//    password = password,
//    profilePictureUri = profilePictureUri
//)

fun User.toUserDetails(): UserDetails = UserDetails(
    firstName = firstName,
    secondName = secondName,
    email = email,
    password = password,
    profilePictureUri = profilePictureUri
)