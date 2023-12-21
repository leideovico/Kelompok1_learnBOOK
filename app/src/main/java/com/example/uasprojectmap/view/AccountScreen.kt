package com.example.uasprojectmap.view

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.uasprojectmap.R
import com.example.uasprojectmap.navigation.Screen
import com.example.uasprojectmap.viewmodel.AccountViewModel

//Container awal layar Account
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun AccountScreen(navController: NavController, accountViewModel: AccountViewModel) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBarAccount(navController) }
    ) {
        //BoxWithConstraints digunakan untuk memberi background image
        //dan menambah padding agar tidak menabrak topbar
        BoxWithConstraints {
            val backgroundImage = painterResource(id = R.drawable.bg2)
            val backgroundAlpha = 0.7f
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Image(
                    painter = backgroundImage,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    alpha = backgroundAlpha,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.medium)
                )

                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                ) {
                    AccountContent(navController, accountViewModel)
                }
            }
        }
    }
}

//Component function untuk Top Bar
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun TopBarAccount(navController: NavController){
    //Variable untuk cek Options Button di klik atau tidak
    var expanded by rememberSaveable { mutableStateOf(false) }

    //Struktur dari TopBarAccount
    Column {
        CenterAlignedTopAppBar(
            navigationIcon = {
                //Tombol untuk kembali ke "home_screen"
                IconButton(onClick = { navController.navigate(Screen.HomeScreen.route){popUpTo(Screen.AccountScreen.route)} }) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = colorResource(id = R.color.light_grey),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(5.dp)
                    )
                }
            },
            title = {
                Text(
                    text = "Account",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.dark_heading)
                )
            },
            actions = {//Option Button
                Box(
                    modifier = Modifier.clickable {
                        expanded = true
                    }
                ) {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Options",
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = colorResource(id = R.color.light_grey),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .padding(5.dp)
                        )
                    }
                    //DropdownMenu is expanded if expanded == true
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .clip(shape = RoundedCornerShape(10.dp))
                            .border(
                                width = 2.dp,
                                color = colorResource(id = R.color.light_grey)
                            )
                    ) {
                        //Plihan untuk logout atau keluar dari aplikasi
                        DropdownMenuItem(
                            onClick = {
                                expanded = false
                                navController.navigate(Screen.LoginScreen.route+"/log_out")
                            },
                            text = {
                                Text("Log Out",
                                    textAlign = TextAlign.Center,
                                    color = colorResource(id = R.color.primary_main)
                                )
                            }
                        )
                        HorizontalDivider(
                            color = colorResource(id = R.color.light_grey),
                            thickness = 1.dp,
                            modifier = Modifier.height(1.dp)
                        )
                        DropdownMenuItem(
                            onClick = {
                                expanded = false
                                navController.navigate("exit_app")
                            },
                            text = { Text("Exit App", textAlign = TextAlign.Center, color = Color.Red, fontWeight = FontWeight.SemiBold) }
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            ),
            scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        )

        // Divider below the toolbar
        HorizontalDivider(
            modifier = Modifier
                .height(1.dp),
            thickness = 1.dp,
            color = colorResource(id = R.color.light_grey)
        )
    }
}


//Konten layar dari Account Screen
@Composable
fun AccountContent(
    navController: NavController,
    accountViewModel: AccountViewModel
){
    //Get data from ViewModel
    val namaLengkap by accountViewModel.namaLengkap.observeAsState("")
    val umur by accountViewModel.umur.observeAsState(0)
    val _noTelpon by accountViewModel.noTelpon.observeAsState("")
    val _favSubject by accountViewModel.favSubject.observeAsState("")

    val phoneNumberState = mutableStateOf(_noTelpon)
    val kesukaanMataPelajaranState = mutableStateOf(_favSubject)

    //Variabel untuk cek berhasil hapus akun atau tidak
    val deleteSuccess by remember(accountViewModel.deleteSuccess.value) { mutableStateOf(accountViewModel.deleteSuccess.value) }
    val uploadImgSuccess by remember(accountViewModel.uploadImgSuccess.value) { mutableStateOf(accountViewModel.uploadImgSuccess.value) }

    var isPhoneNumberValid by rememberSaveable { mutableStateOf(true) }

    // Create temporary states to hold the values entered in the text fields
    var tempPhoneNumber by remember(phoneNumberState.value) { mutableStateOf(phoneNumberState.value) }
    var tempKesukaanMataPelajaran by remember(kesukaanMataPelajaranState.value) { mutableStateOf(kesukaanMataPelajaranState.value) }
    var tempCapturedImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current


    // Create an ActivityResultLauncher for capturing images using GetContent
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { result ->
            result?.let {
                // Image capture was successful, update the temporary profile picture
                tempCapturedImageUri = it
            }
        }
    )

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted, launch the camera to capture an image
            takePictureLauncher.launch("image/*")
        } else {
            // Permission denied, handle accordingly (e.g., show a message to the user)
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    // Text information
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(185.dp)
                .background(Color(android.graphics.Color.parseColor("#6597f9")), shape = CircleShape)
                .padding(12.dp)
                .clickable {
                    // When the circle is clicked, check camera permission
                    requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                },
            contentAlignment = Alignment.Center
        ) {
            // Profile icon goes here (replace with your actual icon)
            if (tempCapturedImageUri != null || accountViewModel.imageUri.value != null) {
                // Display the captured image as the profile picture
                Image(
                    painter = rememberAsyncImagePainter(
                        tempCapturedImageUri ?: Uri.parse(accountViewModel.imageUri.value)
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(185.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.background, shape = CircleShape)
                )

            } else {
                // Display the default icon if no image is captured yet
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(185.dp)
                        .clip(CircleShape)
                        .background(Color(android.graphics.Color.parseColor("#6597f9")), shape = CircleShape)
                )
            }

            // Edit icon in the center
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        //Nama User
        Text(
            text = "$namaLengkap",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 5.dp),
            color = colorResource(id = R.color.dark_heading)
        )
        //Umur User
        Text(
            text = "$umur Years Old",
            fontSize = 16.sp,
            color = colorResource(id = R.color.light_grey),
        )

        // Displayed phone number
        Text(
            text = if (phoneNumberState.value.isNotBlank()) "Phone: ${phoneNumberState.value}" else "",
            fontSize = 16.sp,
            color = colorResource(id = R.color.light_grey),
        )

        // Displayed kesukaan mata pelajaran
        Text(
            text = if (kesukaanMataPelajaranState.value.isNotBlank()) "Favorite Subject: ${kesukaanMataPelajaranState.value}" else "",
            fontSize = 16.sp,
            color = colorResource(id = R.color.light_grey),
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }

    // Input box for phone number
    OutlinedTextField(
        value = tempPhoneNumber,
        onValueChange = {
            if (it.length <= 13) {
                tempPhoneNumber = it
                isPhoneNumberValid = true
            } else {
                isPhoneNumberValid = false
            }
        },
        placeholder = {
            // Placeholder sesuai kebutuhan
            if (tempPhoneNumber.isEmpty()) {
                Text("Phone Number", color = Color.Gray)
            } else {
                // Jika ada konten, tampilkan konten
                Text(tempPhoneNumber)
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        isError = !isPhoneNumberValid,
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF0F0F0),
            unfocusedContainerColor = Color(0xFFF0F0F0),
        )
    )

    // Input box for kesukaan mata pelajaran
    OutlinedTextField(
        value = tempKesukaanMataPelajaran,
        onValueChange = {
            tempKesukaanMataPelajaran = it
        },
        placeholder = {
            // Placeholder sesuai kebutuhan
            if (tempKesukaanMataPelajaran.isEmpty()) {
                Text("Favorite Subject", color = Color.Gray)
            } else {
                // Jika ada konten, tampilkan konten
                Text(tempKesukaanMataPelajaran)
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF0F0F0),
            unfocusedContainerColor = Color(0xFFF0F0F0),
        )
    )

    // Save button
    Button(
        onClick = {
            // Update the actual states when the button is clicked
            accountViewModel.updateNoTelpon(tempPhoneNumber)
            accountViewModel.updateFavSubject(tempKesukaanMataPelajaran)

            // Save the captured image URI only if the user has pressed the save button
            if (tempCapturedImageUri != null) {
                accountViewModel.updateImage(tempCapturedImageUri!!)
                if(uploadImgSuccess){
                    Toast.makeText(context, "Foto berhasil diupload.", Toast.LENGTH_SHORT).show()
                }

            }

            // For now, let's just print them
            println("Phone Number: ${phoneNumberState.value}")
            println("Favorite Subject: ${kesukaanMataPelajaranState.value}")
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .padding(horizontal = 45.dp)
        ,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.primary_main)
        )
    ) {
        Text(text = "SAVE")
    }

    //Button untuk delete account
    Button(
        onClick = {
            //Ketika diklik akun akan di-delete dan menampilkan pesan "Delete akun berhasil". Setelah itu user akan berpindah ke "login_screen"
            accountViewModel.deleteUser()
            if(deleteSuccess){
                Toast.makeText(context, "Delete akun berhasil.", Toast.LENGTH_SHORT).show()
                navController.navigate(Screen.LoginScreen.route){popUpTo(Screen.AccountScreen.route)}
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .padding(horizontal = 50.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFF44336)
        )
    ) {
        Text(text = "DELETE ACCOUNT")
    }

    // Add an Image below the button
    Image(
        painter = painterResource(id = R.drawable.thank), // Replace R.drawable.thanks with your actual image resource
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(100.dp)
    )
}

//Funtion untuk Photo Profile
fun <Painter> rememberImagePainter(capturedImageUri: Uri): Painter {
    return rememberCoilPainter(request = capturedImageUri)
}

fun <Painter> rememberCoilPainter(request: Uri): Painter {
    return rememberImagePainter(request)
}


//Preview
@Composable
fun AccountContentPreview(){
    val accountViewModel = viewModel<AccountViewModel>()
    val navController = rememberNavController()

    AccountContent(navController, accountViewModel)
}