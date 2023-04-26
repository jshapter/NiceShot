package com.example.niceshot.views

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ShutterSpeed
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.niceshot.AppViewModelProvider
import com.example.niceshot.viewmodels.AddPhotoViewModel
import com.example.niceshot.viewmodels.getCamInfo
import com.example.niceshot.viewmodels.validateImage
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPhotoScreen(
    id: Int,
    uri: Uri?,
    navController: NavController,
    viewModel: AddPhotoViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val cameraDevices = getCamInfo(context)
    val image = uri?.let { validateImage(context, it, cameraDevices) }

    var captionState = remember { mutableStateOf(TextFieldValue()) }
    var locationState = remember { mutableStateOf(TextFieldValue()) }

    val getDate = image?.dateTime?.subSequence(0, 10).toString()
    val formattedDate = getDate.replace(':', '-')
    val toDate = LocalDate.parse(formattedDate)
    val dateFormatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Post a photo",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon =  {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                    IconButton(onClick = {navController.navigateUp()}) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Menu",
                            modifier = Modifier.padding(18.dp,0.dp),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .padding(innerPadding)
            ) {
                item {
                    AsyncImage(
                        model = uri,
                        contentDescription = "selected image",
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }
                if (image != null) {
                    if (image.valid) {
                        item {
                            Row(
                                modifier = Modifier
                                    .padding(start = 16.dp, bottom = 16.dp)
                            ) {
                                Icon(
                                    Icons.Filled.CheckCircle,
                                    contentDescription = "",
                                    tint = Color.Green,
                                    modifier = Modifier.padding(end = 3.dp)
                                )
                                Text(
                                    text = "Verified ${Build.MANUFACTURER} ${Build.MODEL}",
                                    color = Color.Green
                                )
                            }
                            Divider(modifier = Modifier.padding(20.dp, 0.dp))
                            Column(
                                modifier = Modifier.padding(20.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(Icons.Filled.DateRange, contentDescription = "")
                                    Text(text = toDate.format(dateFormatter))
                                }
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(Icons.Filled.Camera, contentDescription = "")
                                    Text(text = "f/${image.aperture}")
                                }
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(Icons.Filled.ShutterSpeed, contentDescription = "")
                                    Text(text = "${image.shutterSpeed}s")
                                }
                            }
                            Divider(modifier = Modifier.padding(20.dp, 0.dp))
                        }
                        item {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = "Optional fields:",
                                    fontSize = 14.sp,
                                    fontStyle = FontStyle.Italic
                                )
                                captionState = remember { mutableStateOf(TextFieldValue("")) }
                                OutlinedTextField(
                                    value = captionState.value,
                                    onValueChange = { captionState.value = it },
                                    singleLine = true,
                                    label = { Text(text = "Caption") },
                                    colors = TextFieldDefaults.outlinedTextFieldColors(
                                        focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                                        focusedLabelColor = MaterialTheme.colorScheme.onBackground
                                    )
                                )
                                locationState = remember { mutableStateOf(TextFieldValue("")) }
                                OutlinedTextField(
                                    value = locationState.value,
                                    onValueChange = { locationState.value = it },
                                    singleLine = true,
                                    label = { Text(text = "Location") },
                                    colors = TextFieldDefaults.outlinedTextFieldColors(
                                        focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                                        focusedLabelColor = MaterialTheme.colorScheme.onBackground
                                    )
                                )
                            }
                        }
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {

                                PostPhotoButton(
                                    onSaveClick = {
                                        coroutineScope.launch {
                                            val photoUiState = viewModel.photoUiState
                                            val photoDetails = photoUiState.photoDetails
                                            val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                                            val resolver = context.contentResolver


                                            resolver.takePersistableUriPermission(uri, flags)

                                            photoDetails.uri = uri.toString()
                                            photoDetails.makeModel = "${image.make} " + "${image.model}"
                                            photoDetails.location = locationState.value.text
                                            photoDetails.caption = captionState.value.text
                                            photoDetails.dateTime = toDate.format(dateFormatter)
                                            photoDetails.creatorId = id

                                            photoDetails.aperture = image.aperture.toString()
                                            photoDetails.shutterSpeed =
                                                image.shutterSpeed.toString()

                                            photoDetails.dateUploaded = LocalDate.now().format(dateFormatter)

                                            viewModel.savePhoto()
                                            navController.navigateUp()
                                        }
                                    }
                                )
                            }
                        }

                    } else {
                        item {
                            Row(
                                modifier = Modifier
                                    .padding(start = 16.dp, bottom = 16.dp)
                            ) {
                                Icon(
                                    Icons.Filled.Clear,
                                    contentDescription = "",
                                    tint = Color.Red,
                                    modifier = Modifier.padding(end = 3.dp)
                                )
                                Text(
                                    text = "Metadata does not match this device",
                                    color = Color.Red
                                )
                            }
                            Divider()
                        }
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(50.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column {
                                    Text(
                                        text = "You are unable to post this photo. ",
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = "Find out why.",
                                        style = TextStyle(textDecoration = TextDecoration.Underline),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .clickable { }
                                            .fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PostPhotoButton(
    onSaveClick: () -> Unit
) {
    FloatingActionButton(
        containerColor = MaterialTheme.colorScheme.tertiary,
        onClick = onSaveClick,
        modifier = Modifier.padding(25.dp)
    ) {
        Icon(Icons.Filled.Send, contentDescription = "")
    }
}