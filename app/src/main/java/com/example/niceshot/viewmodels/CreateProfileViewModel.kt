package com.example.niceshot.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.niceshot.data.DataRepository
import com.example.niceshot.data.entities.User

class CreateProfileViewModel (
    private val dataRepository: DataRepository
) : ViewModel() {

    var createProfileUiState by mutableStateOf(UserUiState())
        private set

    suspend fun createAccount() {
        dataRepository.insertUser(createProfileUiState.userDetails.toUser())
    }
}

data class UserUiState(
    val userDetails: UserDetails = UserDetails()
)

data class UserDetails(
    var firstName: String = "",
    var secondName: String = "",
    var email: String = "",
    var password: String = "",
    var profilePictureUri: String = "",
    var created: String = "",
)

fun UserDetails.toUser(): User = User(
    firstName = firstName,
    secondName = secondName,
    email = email,
    password = password,
    profilePictureUri = profilePictureUri,
    created = created
)