package com.example.niceshot.viewmodels

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.niceshot.data.NiceShotDatabase
import kotlinx.coroutines.launch

class SignInViewModel : ViewModel() {

    fun logInAttempt (
        emailState: String,
        passwordState: String,
        db: NiceShotDatabase,
        navController: NavController
    ) {
        viewModelScope.launch {
            try {
                val returnedUser = db.dataDao().getUserByEmail(emailState)

                if (passwordState == returnedUser.password) {
                    val id = returnedUser.userId
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