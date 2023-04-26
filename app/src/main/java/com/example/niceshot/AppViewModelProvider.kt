package com.example.niceshot

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.niceshot.viewmodels.AddPhotoViewModel
import com.example.niceshot.viewmodels.CreateProfileViewModel
import com.example.niceshot.viewmodels.EditProfileViewModel
import com.example.niceshot.viewmodels.FeedViewModel
import com.example.niceshot.viewmodels.SignInViewModel
import com.example.niceshot.viewmodels.ViewPhotoViewModel
import com.example.niceshot.viewmodels.ViewProfileViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {

        initializer {
            AddPhotoViewModel(niceShotApplication().container.dataRepository)
        }

        initializer {
            FeedViewModel(niceShotApplication().container.dataRepository)
        }

        initializer {
            SignInViewModel(niceShotApplication().container.dataRepository)
        }

        initializer {
            CreateProfileViewModel(niceShotApplication().container.dataRepository)
        }

        initializer {
            ViewProfileViewModel(niceShotApplication().container.dataRepository)
        }

        initializer {
            EditProfileViewModel(niceShotApplication().container.dataRepository)
        }

//        initializer {
//            EditPhotoViewModel(niceShotApplication().container.dataRepository)
//        }
        initializer {
            ViewPhotoViewModel(niceShotApplication().container.dataRepository)
        }
    }
}

fun CreationExtras.niceShotApplication(): NiceShotApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as NiceShotApplication)