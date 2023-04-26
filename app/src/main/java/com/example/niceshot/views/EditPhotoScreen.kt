//package com.example.niceshot.views
//
//import android.content.Intent
//import android.net.Uri
//import android.service.controls.ControlsProviderService
//import android.util.Log
//import androidx.activity.result.PickVisualMediaRequest
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.AccountCircle
//import androidx.compose.material.icons.filled.AddCircle
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.Edit
//import androidx.compose.material.icons.filled.Visibility
//import androidx.compose.material.icons.filled.VisibilityOff
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Divider
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextFieldDefaults
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.text.input.TextFieldValue
//import androidx.compose.ui.text.input.VisualTransformation
//import androidx.compose.ui.text.style.TextDecoration
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.core.net.toUri
//import androidx.navigation.NavController
//import coil.compose.AsyncImage
//import com.example.niceshot.AppViewModelProvider
//import com.example.niceshot.viewmodels.AddPhotoViewModel
//import com.example.niceshot.viewmodels.EditPhotoViewModel
//import com.example.niceshot.viewmodels.EditProfileViewModel
//import kotlinx.coroutines.launch
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun EditPhotoScreen (
//    id: Int,
//    photoId: Int,
//    navController: NavController,
//    viewModel: AddPhotoViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = AppViewModelProvider.Factory)
//) {
//
//    val coroutineScope = rememberCoroutineScope()
//
//    val context = LocalContext.current
//
//    val photo = viewModel.getPhoto(photoId)
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        text = "Edit Profile",
//                        fontFamily = FontFamily.Default,
//                        fontSize = 18.sp
//                    )
//                },
//                navigationIcon =  {
//                    IconButton(onClick = { navController.navigateUp() }) {
//                        Icon(
//                            Icons.Filled.ArrowBack,
//                            contentDescription = "Navigate back"
//                        )
//                    }
//                },
//                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
//            )
//        },
//        content = { innerPadding ->
//            Box(modifier = Modifier.padding(innerPadding)) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(25.dp),
//                    verticalArrangement = Arrangement.spacedBy(10.dp)
//                ) {
//
//
//                    Divider()
//
//                    val locationState = remember { mutableStateOf(photo?.let { TextFieldValue(it.location) }) }
//                    locationState.value?.let {
//                        OutlinedTextField(
//                            value = it,
//                            onValueChange = { locationState.value = it },
//                            singleLine = true,
//                            placeholder = {
//                                if (photo != null) {
//                                    Text(photo.location)
//                                }
//                            },
//                            label = { Text(text = "Location") },
//                            colors = TextFieldDefaults.outlinedTextFieldColors(
//                                focusedBorderColor = MaterialTheme.colorScheme.onBackground,
//                                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
//                                focusedLabelColor = MaterialTheme.colorScheme.onBackground
//                            )
//                        )
//                    }
//
//                    val captionState = remember { mutableStateOf(photo?.let { TextFieldValue(it.caption) }) }
//                    captionState.value?.let {
//                        OutlinedTextField(
//                            value = it,
//                            onValueChange = { captionState.value = it },
//                            singleLine = true,
//                            placeholder = {
//                                if (photo != null) {
//                                    Text(text = photo.caption)
//                                }
//                            },
//                            label = { Text(text = "Caption") },
//                            colors = TextFieldDefaults.outlinedTextFieldColors(
//                                focusedBorderColor = MaterialTheme.colorScheme.onBackground,
//                                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
//                                focusedLabelColor = MaterialTheme.colorScheme.onBackground
//                            )
//                        )
//                    }
//
//
//
//                    Box(modifier = Modifier) {
//                        Column(
//                            horizontalAlignment = Alignment.CenterHorizontally
//                        ) {
//                            Button(
//                                modifier = Modifier.padding(top = 15.dp, bottom = 15.dp),
//                                enabled = true,
//                                onClick = {
//
//                                    coroutineScope.launch {
//
//                                    //    navController.navigate(route = "profile_screen/$id/$id/")
//                                    }
//                                },
//                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary)
//                            ) {
//                                Text(
//                                    text = "Save changes",
//                                    color = Color.White
//                                )
//                            }
//                            Text(
//                                text = "Delete photo",
//                                fontSize = 12.sp,
//                                style = TextStyle(textDecoration = TextDecoration.Underline),
//                                color = Color.Red,
//                                modifier = Modifier
//                                    .clickable {
//                                        coroutineScope.launch {
//                                            if (id != null) {
//                                                viewModel.deletePhoto(photoId)
//                                            }
//                                        }
//                                        navController.navigate(route = "feed_screen/$id/")
//                                    }
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    )
//
//}