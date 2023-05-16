package com.example.niceshot.views

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.niceshot.AppViewModelProvider
import com.example.niceshot.data.entities.Photo
import com.example.niceshot.data.entities.User
import com.example.niceshot.viewmodels.FeedViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    id: Int,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: FeedViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val view = LocalView.current
    SideEffect {
        forceShowSystemBar(view)
    }

    val homeUiState by viewModel.feedUiState.collectAsState()
    val scope =  rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val items = listOf(Icons.Default.Info, Icons.Default.Settings, Icons.Default.Logout)
    val selectedItem = remember { mutableStateOf(items[2]) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Box(modifier = Modifier) {
                    Text(
                        text = "NiceShot!",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(20.dp, top = 35.dp)
                    )
                }
                Spacer(
                    Modifier.height(10.dp)
                )

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "settings") },
                    label = { Text(text = "Settings") },
                    selected = Icons.Default.Settings == selectedItem.value,
                    onClick = {
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Info, contentDescription = "app info") },
                    label = { Text(text = "About") },
                    selected = Icons.Default.Info == selectedItem.value,
                    onClick = {
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Logout, contentDescription = "log out") },
                    label = { Text(text = "Logout") },
                    selected = Icons.Default.Logout == selectedItem.value,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            navController.navigate(route = "sign_in_screen") {
                                popUpTo(0)
                            }
                        }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        },
        content = {
            Scaffold(
                topBar = {
                    AppBar(
                        navController,
                        id = id,
                        onNavigationIconClick = {
                            scope.launch {
                                drawerState.open()
                            }
                            Log.d(TAG, "Menu Pressed!")
                        }
                    )
                },
                bottomBar = {
                    BottomBar(
                        id,
                        navController
                    )
                },
            ) { innerPadding ->
                FeedBody(
                    photoList = homeUiState.photoList,
                    modifier = modifier.padding(innerPadding),
                    id = id,
                    navController = navController
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar (
    navController: NavController,
    id: Int,
    onNavigationIconClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row {
                Text(
                    text = "NiceShot!",
                    style = MaterialTheme.typography.titleSmall,

                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(8.dp, top = 4.dp)
                )
            }
        },
        navigationIcon =  {
            IconButton(
                onClick = { onNavigationIconClick() }
            ) {
                Icon(
                    Icons.Outlined.Menu,
                    contentDescription = "menu"
                )
            }
        },
        actions = {
            IconButton(onClick = {   navController.navigate(route = "profile_screen/$id/$id/")  }) {
                Icon(
                    Icons.Filled.Person,
                    contentDescription = "profile",
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
    )
}

@Composable
fun BottomBar(
    id: Int,
    navController: NavController
) {
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val pickPhoto = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        selectedImageUri = uri
    }

    Row(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.primary)
        .padding(22.dp, 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    )
    {
        IconButton(
            onClick = {  },
            )
        {
            Icon(
                Icons.Outlined.Bookmark,
                contentDescription = "save",
                modifier = Modifier.size(30.dp)
            )
        }
        FloatingActionButton(
            modifier = Modifier
                .padding(10.dp),
            containerColor = MaterialTheme.colorScheme.tertiary,
            onClick = { pickPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.SingleMimeType("image/jpeg"))) }
        )
        {
            Icon(
                Icons.Outlined.Add,
                contentDescription = "Menu",
                modifier = Modifier.size(48.dp)
            )
        }
        IconButton(
            onClick = {  },
        )
        {
            Icon(
                Icons.Outlined.Group,
                contentDescription = "circles",
                modifier = Modifier.size(30.dp)
            )
        }
        if (selectedImageUri != null) {
            val encodedUri = Uri.encode(selectedImageUri.toString())
            selectedImageUri = null
            navController.navigate(route = "add_photo_screen/$id/$encodedUri/")
        }
    }
}

@Composable
private fun FeedBody(
    photoList: List<Photo>,
    modifier: Modifier = Modifier,
    id: Int,
    navController: NavController
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (photoList.isEmpty()) {
            Text(
                text = "no content to show"
            )
        } else {
            PhotoList(
                photoList = photoList,
                id = id,
                navController = navController
            )
        }
    }
}

@Composable
fun PhotoList(
    photoList: List<Photo>,
    modifier: Modifier = Modifier,
    viewModel: FeedViewModel = viewModel(factory = AppViewModelProvider.Factory),
    id: Int,
    navController: NavController
) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(items = photoList, key = { it.photoId }) { photo ->
            PhotoCard(
                photo = photo,
                user = viewModel.returnUser(photo.creatorId),
                id = id,
                navController = navController
            )
        }
    }
}

@Composable
fun PhotoCard(
    photo: Photo,
    user: User,
    navController: NavController,
    id: Int
) {
    Card(
        modifier = Modifier
            .padding(12.dp)
            .wrapContentSize(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
    ) {
        Column(modifier = Modifier) {
            Row(
                modifier = Modifier
                    .padding(10.dp, 2.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (user.profilePictureUri != "null") {
                    AsyncImage(
                        model = user.profilePictureUri,
                        contentDescription = "Profile Photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.tertiary, CircleShape)
                            .clickable {
                                navController.navigate(route = "profile_screen/$id/${user.userId}/")
                            }
                    )
                } else {
                    IconButton(
                        onClick = { navController.navigate(route = "profile_screen/$id/${user.userId}/") }
                    ) {
                        Icon(
                            Icons.Filled.AccountCircle,
                            contentDescription = "no profile picture",
                            modifier = Modifier
                                .size(48.dp)
                                .border(2.dp, MaterialTheme.colorScheme.secondary, CircleShape)
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    Text(
                        text = user.firstName + " " + user.secondName,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = photo.dateTime,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light
                    )
                }
                Spacer(modifier = Modifier.weight(1F))
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        Icons.Filled.MoreVert,
                        contentDescription = "options"
                    )
                }
            }
            AsyncImage(
                model = photo.uri,
                contentDescription = "photo",
                modifier = Modifier.clickable {
                    val uriParse = Uri.parse(photo.uri)
                    val encodedUri = Uri.encode(uriParse.toString())
                    navController.navigate(route = "view_photo_screen/$encodedUri/")
                }
            )
            Column(modifier = Modifier.padding(10.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (photo.location != "") {
                                Icon(
                                    Icons.Filled.Place,
                                    contentDescription = "location",
                                    modifier = Modifier.size(22.dp)
                                )
                                Text(
                                    text = photo.location,
                                    fontSize = 18.sp
                                )
                            }
                        }
                        Text(
                            text = photo.makeModel,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Light,
                            modifier = Modifier
                                .padding(4.dp, 0.dp)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1F))
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            Icons.Filled.ExpandLess,
                            contentDescription = "see more"
                        )
                    }
                }

                if (photo.caption != "") {
                    Divider(
                        thickness = 1.dp,
                        modifier = Modifier.padding(10.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = "\"${photo.caption}\"",
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier
                            .padding(12.dp, 0.dp)
                    )
                    Divider(
                        thickness = 1.dp,
                        modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 0.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Row {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Outlined.AddAPhoto, contentDescription = "")
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Outlined.PersonAdd, contentDescription = "")
                    }
                }
            }
        }
    }
}