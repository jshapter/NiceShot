package com.example.niceshot.viewmodels

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.niceshot.data.DataRepository
import com.example.niceshot.data.NiceShotDatabase
import kotlinx.coroutines.launch

class SignInViewModel(
    private val photoRepository: DataRepository
) : ViewModel() {

    fun logInAttempt (
        emailState: String,
        passwordState: String,
        db: NiceShotDatabase,
        navController: NavController
    ) {
        viewModelScope.launch {
            try {

                Log.d(ContentValues.TAG, "Trying email: $emailState")
                val returnedUser = db.dataDao().getUserByEmail(emailState)

                if (passwordState == returnedUser.password) {
                    val id = returnedUser.userId
                    Log.d(ContentValues.TAG, "ID set: $id")
                    Log.d(ContentValues.TAG, "Logged in as: ${returnedUser.firstName}")
                    navController.navigate(route = "feed_screen/$id/")
                } else {
                    Log.d(ContentValues.TAG, "INCORRECT PASSWORD")
                }
            } catch(e: java.lang.NullPointerException) {
                Log.e(ContentValues.TAG, "USER NOT FOUND")
            }
        }
    }
}