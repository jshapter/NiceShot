package com.example.niceshot.ui

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.niceshot.views.AddPhotoScreen
import com.example.niceshot.views.CreateProfileScreen
import com.example.niceshot.views.EditProfileScreen
import com.example.niceshot.views.FeedScreen
import com.example.niceshot.views.ProfileScreen
import com.example.niceshot.views.SignInScreen
import com.example.niceshot.views.ViewPhotoScreen


@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.SignInScreen.route) {

        composable(
            route = Screen.SignInScreen.route
        ) {
            SignInScreen(
                navController = navController
            )
        }

        composable(
            route = Screen.CreateProfileScreen.route
        ) {
            CreateProfileScreen(
                navController = navController
            )
        }

        composable(
            route = Screen.FeedScreen.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                }
            )
        ) {
            val id = it.arguments?.getInt("id")
            if (id != null) {
                FeedScreen(
                    id = id,
                    navController = navController
                )
            }
        }
        composable(
            route = Screen.AddPhotoScreen.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                },
                navArgument("uri") {
                    type = NavType.StringType
                }
            )
        ) {
            val id = it.arguments?.getInt("id")
            val uri = Uri.parse(it.arguments?.getString("uri"))
            if (id != null) {
                AddPhotoScreen(
                    id = id,
                    uri = uri,
                    navController = navController
                )
            }
        }
        composable(
            route = Screen.ProfileScreen.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                },
                navArgument("selectedId") {
                    type = NavType.IntType
                }
            )
        ) {
            val id = it.arguments?.getInt("id")
            val selectedId = it.arguments?.getInt("selectedId")
            if (id != null) {
                if (selectedId != null) {
                    ProfileScreen(
                        id = id,
                        selectedId = selectedId,
                        navController = navController
                    )
                }
            }
        }
        composable(
            route = Screen.ViewPhotoScreen.route,
            arguments = listOf(
                navArgument("uri") {
                    type = NavType.StringType
                }
            )
        ) {
            val uri = it.arguments?.getString("uri")
            if (uri != null) {
                ViewPhotoScreen(
                    uri = uri,
                )
            }
        }
        composable(
            route = Screen.EditProfileScreen.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                }
            )
        ) {
            val id = it.arguments?.getInt("id")
            if (id != null) {
                EditProfileScreen(
                    id = id,
                    navController = navController
                )
            }
        }
    }
}