package com.example.niceshot.views

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.niceshot.AppViewModelProvider
import com.example.niceshot.data.entities.Photo
import com.example.niceshot.data.entities.User
import com.example.niceshot.viewmodels.ViewProfileViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    id: Int,
    selectedId: Int,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ViewProfileViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val view = LocalView.current
    SideEffect {
        forceShowSystemBar(view)
    }

    val user = selectedId.let { viewModel.returnUser(it) }

    var ownAccount = false
    if (id == selectedId) {
        ownAccount = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = ""
                    )
                },
                navigationIcon =  {
                    IconButton(onClick = { navController.navigate(route = "feed_screen/$id/") }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                },
                actions = {
                    if (ownAccount) {
                        IconButton(onClick = { navController.navigate(route = "edit_profile_screen/$id/") }) {
                            Icon(
                                Icons.Filled.Edit,
                                contentDescription = ""
                            )
                        }
                    } else {
                        IconButton(onClick = {  }) {
                            Icon(
                                Icons.Filled.PersonAdd,
                                contentDescription = ""
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                ProfileBody(id = id, user = user, viewModel = viewModel, selectedId = selectedId, modifier = modifier, navController = navController)
            }

        }
    )
}

@Composable
fun ProfileBody(
    id: Int,
    user: User,
    viewModel: ViewProfileViewModel,
    selectedId: Int,
    modifier: Modifier,
    navController: NavController
) {

    val photoList = selectedId.let { viewModel.userPhotos(it) }

    Column(
        modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {

            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    user.profilePictureUri.let { Log.d(ContentValues.TAG, "propic uri: $it") }
                    if (user.profilePictureUri != "null") {
                        AsyncImage(
                            model = user.profilePictureUri,
                            contentDescription = "Profile picture.",
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape)
                                .border(4.dp, MaterialTheme.colorScheme.tertiary, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            Icons.Filled.AccountCircle,
                            contentDescription = "",
                            modifier
                                .size(150.dp)
                                .border(4.dp, MaterialTheme.colorScheme.tertiary, CircleShape)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${user.firstName} ${user.secondName}",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Divider(modifier.padding(20.dp, 10.dp))
                Row(
                    modifier
                        .padding(start = 20.dp)
                        .clickable { },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.DateRange,
                        contentDescription = "",
                        modifier.padding(5.dp)
                    )
                    Text(
                        text = "Member since: ${user.created}"
                    )
                }
                Divider(modifier.padding(20.dp, 10.dp))
            }

            if (photoList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "no photos",
                    )
                }

            } else {
                PhotoGrid(
                    id = id,
                    photoList = photoList,
                    navController = navController
                )
            }

    }
}

@Composable
fun PhotoGrid(
    id: Int,
    photoList: List<Photo>,
    navController: NavController
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(100.dp),
        contentPadding = PaddingValues(
            start = 12.dp,
            top = 12.dp,
            end = 12.dp,
            bottom = 12.dp
        )
    ) {
        items(photoList, key = { it.photoId}) { photo ->
            Thumbnail(
                id = id,
                photo = photo,
                navController = navController,
            )
        }
    }
}

@Composable
fun Thumbnail(
    id: Int,
    photo: Photo,
    navController: NavController
) {
    AsyncImage(
        model = Uri.parse(photo.uri),
        contentDescription = "",
        modifier = Modifier
            .clickable {
                val uriParse = Uri.parse(photo.uri)
                val encodedUri = Uri.encode(uriParse.toString())
                navController.navigate(route = "view_photo_screen/$encodedUri/")
            }
            .border(4.dp, MaterialTheme.colorScheme.onBackground)
            .size(100.dp),
        contentScale = ContentScale.Crop
    )
}

fun forceShowSystemBar(view: View) {
    val window = view.context.getActivity()!!.window
    WindowCompat.getInsetsController(window, view).show(
        WindowInsetsCompat.Type.statusBars() or
                WindowInsetsCompat.Type.navigationBars()
    )
}

fun Context.getActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}