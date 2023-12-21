package com.example.uasprojectmap.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.uasprojectmap.MainActivity
import com.example.uasprojectmap.MainAppActivity
import com.example.uasprojectmap.R
import com.example.uasprojectmap.navigation.Screen
import com.example.uasprojectmap.viewmodel.HomeViewModel


//Container awal layar Home
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel) {
    val nameLengkap by homeViewModel.namaLengkap.observeAsState("")
    val umur by homeViewModel.umur.observeAsState(0)
    val photoProfile by homeViewModel.imageUri.observeAsState("")

    Scaffold( //Untuk struktur app. Based on Material 3
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBarHome(
                navController,
                nameLengkap,
                umur,
                photoProfile
            )
        }
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
                    .padding(horizontal = 20.dp)
                ) {
                    HomeContent()
                }
            }
        }
    }
}

//Component function untuk TopBar di Home
@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarHome(navController: NavController, namaLengkap: String, umur: Int, photo: String){
    //Variable untuk cek Options Button di klik atau tidak
    var expanded by rememberSaveable { mutableStateOf(false) }
    // Create a state to track whether the dialog is open
    var isDialogOpen by remember { mutableStateOf(false) }

    // Dialog to display enlarged profile picture
    if (isDialogOpen) {
        Dialog(
            onDismissRequest = {
                // Dismiss the dialog when clicked outside
                isDialogOpen = false
            }
        ) {
            Image(
                painter = rememberAsyncImagePainter(Uri.parse(photo)),
                contentDescription = "User",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .aspectRatio(1f)
                    .clickable {
                        // Close the dialog when clicked
                        isDialogOpen = false
                    }
            )
        }
    }

    //Struktur Front-end TopBar
    Column{
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray, CircleShape)
                            .clickable {
                                // Open the dialog when clicked
                                isDialogOpen = true
                            }
                    ) {
                        if(photo != null) {
                            Image(
                                painter = rememberAsyncImagePainter(Uri.parse(photo)),
                                contentDescription = "User",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(
                                        MaterialTheme.colorScheme.background,
                                        CircleShape
                                    ) // Menyamarkan bagian di luar lingkaran
                            )
                        } else {
                            Image(
                                imageVector = Icons.Filled.AccountCircle,
                                contentDescription = "User",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(48.dp),
                            )
                        }
                    }

                    Box(modifier = Modifier.padding(start = 8.dp)) {
                        // Use the passed-in information
                        Text(text = "Hi, $namaLengkap", fontSize = 17.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 10.dp))
                        Text(text = "$umur Years Old", fontSize = 13.sp, modifier = Modifier.padding(top = 20.dp))
                    }
                }
            },
            actions = {//Options Button
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

        HorizontalDivider(
            color = colorResource(id = R.color.light_grey),
            thickness = 1.dp,
            modifier = Modifier.height(1.dp)
        )
    }
}




//Preview
@Composable
fun HomeScreenPreview(){
    val navController = rememberNavController()
    val homeViewModel = viewModel<HomeViewModel>()
    HomeScreen(navController, homeViewModel)
}

@Preview
@Composable
fun HomeContent() {
    //Struktur dari HomeContent()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) //Modifier untuk membuat bisa scroll secara vertikal jika konten melebihi layar
    ) {
        // Banner
        Image(
            painter = painterResource(id = R.drawable.banner),
            contentDescription = "Banner",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        HorizontalDivider(
            thickness = 1.dp,
            color = colorResource(id = R.color.light_grey)
        )

        // Subjudul baru "Visi & Misi"
        Text(
            text = "VISI & MISI",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .wrapContentWidth(Alignment.CenterHorizontally)
        )

        HorizontalDivider(
            thickness = 1.dp,
            color = colorResource(id = R.color.light_grey)
        )

        // Visi & Misi
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(color = Color(0xFF6597F9), shape = RoundedCornerShape(8.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart)
            ) {
                Text(
                    text = "VISI",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 16.dp,  // Tetapkan padding top sesuai kebutuhan Anda
                            bottom = 3.dp  // Ganti padding bottom menjadi 8.dp
                        )
                        .align(Alignment.CenterHorizontally)
                )
                Text(
                    text = """
                1. Menjadi pionir dalam memberdayakan anak-anak panti asuhan melalui literasi, memberikan akses setara terhadap pendidikan yang berkualitas.
                
                2. Mengubah paradigma pendidikan inklusif dengan memanfaatkan teknologi, menciptakan ruang belajar yang merangsang, inklusif, dan mendorong pertumbuhan intelektual.
                
                3. Menjadi percontohan dalam meningkatkan literasi anak-anak panti asuhan, memberikan dampak positif pada tingkat literasi global, dan menciptakan masa depan yang cerah.
            """.trimIndent(),
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Justify

                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(bottom = 25.dp) // Penambahan padding bottom sebesar 25.dp
                .background(color = Color(0xFF6597F9), shape = RoundedCornerShape(8.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart)
            ) {
                Text(
                    text = "MISI",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 16.dp,
                            bottom = 3.dp
                        )
                        .align(Alignment.CenterHorizontally)
                )
                Text(
                    text = """
                1. Menyediakan platform e-learning yang memastikan setiap anak panti asuhan mendapatkan pendidikan berkualitas tanpa terhambat oleh keterbatasan fasilitas.
                
                2. Terus mengembangkan dan menyempurnakan metode pembelajaran inovatif untuk meningkatkan keterampilan literasi, kreativitas, dan pemecahan masalah anak-anak panti asuhan.
                
                3. Berkolaborasi dengan lembaga pendidikan, panti asuhan, dan organisasi non-profit untuk menciptakan ekosistem yang mendukung pertumbuhan literasi anak-anak panti asuhan secara holistik.
            """.trimIndent(),
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
}

@Preview
@Composable
fun TopBarHomePreview(){
    val navController = rememberNavController()
    TopBarHome(navController, "NAMA", 10, "")
}