package com.example.niceshot.views

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.niceshot.AppViewModelProvider
import com.example.niceshot.data.DataDao
import com.example.niceshot.data.NiceShotDatabase
import com.example.niceshot.ui.theme.Lips
import com.example.niceshot.viewmodels.CreateProfileViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProfileScreen (
    navController: NavController,
    viewModel: CreateProfileViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = AppViewModelProvider.Factory)
) {

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    val dao: DataDao = NiceShotDatabase.getDatabase(context).dataDao()

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val pickPhoto = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        selectedImageUri = uri
    }
    

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(25.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if (selectedImageUri != null) {
                    AsyncImage(model = selectedImageUri,
                        contentDescription = "",
                        modifier = Modifier
                            .size(128.dp)
                            .clip(CircleShape)
                            .border(2.dp, Lips, CircleShape)
                            .clickable {
                                pickPhoto.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.SingleMimeType(
                                            "image/jpeg"
                                        )
                                    )
                                )
                            },
                        contentScale = ContentScale.Crop
                    )
                } else {
                    IconButton(
                        onClick = { pickPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.SingleMimeType("image/jpeg"))) },
                        modifier = Modifier
                            .size(128.dp)
                    ) {
                        Icon(Icons.Outlined.AccountCircle,
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.clickable { pickPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.SingleMimeType("image/jpeg"))) }
            ) {
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
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Divider(
                modifier = Modifier.padding(0.dp, 10.dp),
                color = MaterialTheme.colorScheme.onBackground
            )

            val firstNameState = remember { mutableStateOf(TextFieldValue()) }
            OutlinedTextField(
                value = firstNameState.value,
                onValueChange = { firstNameState.value = it },
                singleLine = true,
                label = { Text(text = "First name *") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    focusedLabelColor = MaterialTheme.colorScheme.onBackground
                )
            )

            val secondNameState = remember { mutableStateOf(TextFieldValue()) }
            OutlinedTextField(
                value = secondNameState.value,
                onValueChange = { secondNameState.value = it },
                singleLine = true,
                label = { Text(text = "Second name *") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    focusedLabelColor = MaterialTheme.colorScheme.onBackground
                )
            )

            val emailState = remember { mutableStateOf(TextFieldValue()) }
            OutlinedTextField(
                value =  emailState.value,
                onValueChange = { emailState.value = it },
                singleLine = true,
                label = { Text(text = "E-mail *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    focusedLabelColor = MaterialTheme.colorScheme.onBackground
                )
            )

            var passwordVisible by rememberSaveable { mutableStateOf(false) }
            val passwordState = remember { mutableStateOf(TextFieldValue()) }
            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                label = { Text("Password *") },
                singleLine = true,
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

            Box(modifier = Modifier) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(
                        modifier = Modifier.padding(top = 15.dp, bottom = 15.dp),
                        enabled = true,
                        onClick = {
                            coroutineScope.launch {

                                val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                                val resolver = context.contentResolver
                                selectedImageUri?.let { resolver.takePersistableUriPermission(it, flags) }

                                val createProfileUiState = viewModel.createProfileUiState
                                val userDetails = createProfileUiState.userDetails

                                userDetails.firstName = firstNameState.value.text
                                userDetails.secondName = secondNameState.value.text
                                userDetails.email = emailState.value.text
                                userDetails.password = passwordState.value.text
                                userDetails.profilePictureUri = selectedImageUri.toString()

                                val dateFormatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy")
                                userDetails.created = LocalDate.now().format(dateFormatter)

                                viewModel.createAccount()

                                val user = dao.getUserByEmail(emailState.value.text)
                                val id = user.userId

                                navController.navigate(route = "feed_screen/$id/")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary)

                    ) {
                        Text(
                            text = "Create account",
                            color = Color.White
                        )
                    }
                    Text(
                        text = "Log in to existing account",
                        fontSize = 12.sp,
                        modifier = Modifier.clickable { navController.navigateUp() },
                    )
                }
            }
        }
    }
}