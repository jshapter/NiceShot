package com.example.niceshot.ui

sealed class Screen(val route: String) {
    object FeedScreen : Screen("feed_screen/{id}/")
    object AddPhotoScreen : Screen("add_photo_screen/{id}/{uri}/")
    object SignInScreen : Screen("sign_in_screen")
    object ProfileScreen : Screen("profile_screen/{id}/{selectedId}/")
    object CreateProfileScreen : Screen("create_profile_screen")
    object EditProfileScreen : Screen("edit_profile_screen/{id}/")
    object ViewPhotoScreen : Screen("view_photo_screen/{uri}/")
}
