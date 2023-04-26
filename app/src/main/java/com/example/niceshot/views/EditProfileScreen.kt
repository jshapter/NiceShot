package com.example.niceshot.views

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.service.controls.ControlsProviderService
import android.text.style.UnderlineSpan
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.niceshot.AppViewModelProvider
import com.example.niceshot.viewmodels.EditProfileViewModel
import kotlinx.coroutines.launch
import java.time.format.TextStyle


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen (
    id: Int?,
    navController: NavController,
    viewModel: EditProfileViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    val user = id?.let { viewModel.returnUser(it) }

    val photoList = id.let {
        if (it != null) {
            viewModel.userPhotos(it)
        }
    }

    Log.d(TAG, "photolist: ${photoList}")

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val pickPhoto = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        selectedImageUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Edit Profile",
                        fontFamily = FontFamily.Default,
                        fontSize = 18.sp
                    )
                },
                navigationIcon =  {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(25.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (selectedImageUri != null) {
                        AsyncImage(model = selectedImageUri,
                            contentDescription = "",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .border(4.dp, MaterialTheme.colorScheme.tertiary, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else if (user != null) {
                        if (user.profilePictureUri != "null") {
                            AsyncImage(
                                model = user.profilePictureUri,
                                contentDescription = "",
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .border(4.dp, MaterialTheme.colorScheme.tertiary, CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            IconButton(
                                modifier = Modifier.size(128.dp),
                                onClick = {
                                    pickPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.SingleMimeType("image/jpeg")))
                                }
                            ) {
                                Icon(
                                    Icons.Filled.AccountCircle,
                                    contentDescription = "profile",
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                            .clickable {
                                pickPhoto.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.SingleMimeType(
                                            "image/jpeg")
                                    )
                                )
                            }
                    )
                    {
                        if (selectedImageUri != null) {
                            Icon(
                                Icons.Filled.Edit, contentDescription = ""
                            )
                        } else {
                            Icon(
                                Icons.Filled.AddCircle, contentDescription = ""
                            )
                        }

                        Text(
                            text = "profile picture",
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }

                    Divider()

                    val firstNameState = remember { mutableStateOf(user?.let { TextFieldValue(it.firstName) }) }
                    firstNameState.value?.let {
                        OutlinedTextField(
                            value = it,
                            onValueChange = { firstNameState.value = it },
                            singleLine = true,
                            placeholder = {
                                if (user != null) {
                                    Text(user.firstName)
                                }
                            },
                            label = { Text(text = "First name *") },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                                focusedLabelColor = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    }

                    val secondNameState = remember { mutableStateOf(user?.let { TextFieldValue(it.secondName) }) }
                    secondNameState.value?.let {
                        OutlinedTextField(
                            value = it,
                            onValueChange = { secondNameState.value = it },
                            singleLine = true,
                            placeholder = {
                                if (user != null) {
                                    Text(text = user.secondName)
                                }
                            },
                            label = { Text(text = "Second name *") },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                                focusedLabelColor = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    }

                    val emailState = remember { mutableStateOf(user?.let { TextFieldValue(it.email) }) }
                    emailState.value?.let {
                        OutlinedTextField(
                            value = it,
                            onValueChange = { emailState.value = it },
                            singleLine = true,
                            placeholder = {
                                if (user != null) {
                                    Text(text = user.email)
                                }
                            },
                            label = { Text(text = "E-mail *") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                                focusedLabelColor = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    }

                    var passwordVisible by rememberSaveable { mutableStateOf(false) }
                    val passwordState = remember { mutableStateOf(user?.let { TextFieldValue(it.password) }) }
                    passwordState.value?.let {
                        OutlinedTextField(
                            value = it,
                            onValueChange = { passwordState.value = it },
                            label = { Text("Password *") },
                            singleLine = true,
                            placeholder = {
                                if (user != null) {
                                    Text(text = user.password)
                                }
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            trailingIcon = {
                                val image = if (passwordVisible)
                                    Icons.Filled.Visibility
                                else Icons.Filled.VisibilityOff
                                IconButton(onClick = {passwordVisible = !passwordVisible}){
                                    Icon(imageVector  = image, "")
                                }
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                                focusedLabelColor = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    }

                    Box(modifier = Modifier) {
                        Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
                                modifier = Modifier.padding(top = 15.dp, bottom = 15.dp),
                                enabled = true,
                                onClick = {

                                    coroutineScope.launch {

                                        val createProfileUiState = viewModel.editProfileUiState
                                        val userDetails = createProfileUiState.userDetails

                                        if (selectedImageUri != null) {
                                            val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                                            val resolver = context.contentResolver
                                            selectedImageUri?.let { resolver.takePersistableUriPermission(it, flags) }
                                            userDetails.profilePictureUri = selectedImageUri.toString()
                                        }

                                        if (selectedImageUri != null) {
                                            val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                                            val resolver = context.contentResolver
                                            selectedImageUri?.let { resolver.takePersistableUriPermission(it, flags) }
                                            userDetails.profilePictureUri = selectedImageUri.toString()
                                        } else {
                                            if (user != null) {
                                                selectedImageUri = user.profilePictureUri.toUri()
                                            }
                                        }

                                        Log.d(ControlsProviderService.TAG, "ATTEMPTING UPDATE!")
                                        firstNameState.value?.let {
                                            secondNameState.value?.let { it1 ->
                                                emailState.value?.let { it2 ->
                                                    passwordState.value?.let { it3 ->
                                                        if (id != null) {
                                                            viewModel.updateAccount(
                                                                it.text,
                                                                it1.text,
                                                                it2.text,
                                                                it3.text,
                                                                selectedImageUri.toString(),
                                                                id
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        Log.d(ControlsProviderService.TAG, "UPDATE SHOULD BE COMPLETE!")

                                        navController.navigate(route = "profile_screen/$id/$id/")
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary)
                            ) {
                                Text(
                                    text = "Save changes",
                                    color = Color.White
                                )
                            }
                            Text(
                                text = "Delete account",
                                fontSize = 12.sp,
                                style = androidx.compose.ui.text.TextStyle(textDecoration = TextDecoration.Underline),
                                color = Color.Red,
                                modifier = Modifier
                                    .clickable {

                                        coroutineScope.launch {



                                            if (id != null) {
                                                viewModel.deleteUser(id)
                                            }
                                        }
                                        navController.navigate(route = "sign_in_screen") {
                                            popUpTo(0)
                                        }
                                    }
                            )
                        }
                    }
                }
            }
        }
    )
}