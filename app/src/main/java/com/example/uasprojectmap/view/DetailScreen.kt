package com.example.uasprojectmap.view

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.uasprojectmap.R
import com.example.uasprojectmap.model.Book
import com.example.uasprojectmap.model.Journal
import com.example.uasprojectmap.model.Publication
import com.example.uasprojectmap.navigation.Screen

//Container awal layar Detail dari Buku atau Jurnal yang dipilih
@Composable
fun DetailScreen(navController: NavController, selectedPublication: Publication){
    Scaffold(
        topBar = { TopBarDetails(navController = navController, selectedPublication = selectedPublication)}
    ) {
        //Column digunakan untuk menambah padding agar tidak menabrak topbar
        Column(modifier = Modifier
            .padding(it)
            .padding(top = 31.dp)
            .padding(horizontal = 20.dp)
        ) {
            DetailScreenContent(selectedPublication = selectedPublication)
        }
    }
}

//Component function untuk Top Bar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarDetails(navController: NavController, selectedPublication: Publication){
    var expanded by rememberSaveable { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally){
        CenterAlignedTopAppBar(
            navigationIcon = {
                //Tombol untuk kembali ke tampilan list buku atau list jurnal
                IconButton(onClick = {
                    when(selectedPublication){ //Cek tipe dari publication yang dipilih
                        is Book -> {
                            navController.navigate(Screen.BookListScreen.route){popUpTo(Screen.DetailBookScreen.route){inclusive = true} }
                        }
                        is Journal -> {
                            navController.navigate(Screen.JournalListScreen.route){popUpTo(Screen.DetailJournalScreen.route){inclusive = true}}
                        }
                    }
                }) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        modifier = Modifier
                            .border(
                                width = 1.dp, color = colorResource(id = R.color.light_grey),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(5.dp)
                    )
                }
            },
            title = {
                Text(
                    text = selectedPublication.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.dark_heading),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
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

        HorizontalDivider(
            thickness = 1.dp,
            color = colorResource(id = R.color.light_grey)
        )
    }
}

////konten layar untuk menampilkan Buku atau Jurnal yang dipilih
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun DetailScreenContent(selectedPublication: Publication){
    //Variable untuk menyimpan dari buku atau jurnal yang dipilih
    var urlImg = selectedPublication.url_img
    var title = selectedPublication.title
    var author = selectedPublication.author
    var description = selectedPublication.description
    var urlPdf = selectedPublication.url_pdf

    //Variable untuk membuka PDF melalui Browser. Buku didownload. Jurnal dibaca online.
    val context = LocalContext.current
    val intent = remember { Intent(Intent.ACTION_VIEW, Uri.parse(urlPdf)) }

    //Struktur DetailScreenContent()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Gambar Buku atau Jurnal yang dipilih
        Box(modifier = Modifier
            .width(135.dp)
            .height(179.dp)
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .align(Alignment.CenterHorizontally),
            content = { AsyncImage(
                model = urlImg,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )}
        )
        //Judul Buku atau Jurnal
        Text(
            text = title,
            fontWeight = FontWeight(600),
            fontSize = 16.sp,
            color = colorResource(id = R.color.dark_heading),
            lineHeight = 15.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 10.dp)
        )
        //Nama penulis
        Text(
            text = "by $author",
            fontWeight = FontWeight(500),
            fontSize = 12.sp,
            color = colorResource(id = R.color.light_grey),
            lineHeight = 10.sp,
            textAlign = TextAlign.Center
        )
        //Box untuk deskripsi
        Box(modifier = Modifier
            .padding(top = 10.dp)
            .border(
                width = 1.dp,
                color = Color(0xFFE3E3E3),
                shape = RoundedCornerShape(size = 12.dp)
            )
            .background(
                color = Color(0xFFFAFAFC),
                shape = RoundedCornerShape(size = 12.dp)
            )
        ) {
            Column(modifier = Modifier.padding(all = 12.dp)) {
                Text(
                    text = "About",
                    fontSize = 16.sp,
                    fontWeight = FontWeight(700),
                    color = Color(0xFF28293D)
                )
                Text(
                    text = description,
                    fontSize = 12.sp,
                    lineHeight = 21.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF555770)
                )
            }
        }
        //Tombol untuk membaca Buku atau Jurnal
        Button(
            onClick = { context.startActivity(intent) }, //ketika diklik, aplikasi akan berpindah ke browser melalui intent
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3E7BFA),
                contentColor = Color(0xFFFFFFFF)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .padding(top = 10.dp)
        ) {
            Text(text = "Start Reading")
        }
    }
}