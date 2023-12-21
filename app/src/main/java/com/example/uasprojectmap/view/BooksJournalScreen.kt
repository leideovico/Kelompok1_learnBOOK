package com.example.uasprojectmap.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.uasprojectmap.R
import com.example.uasprojectmap.model.Publication
import com.example.uasprojectmap.navigation.Screen
import com.example.uasprojectmap.viewmodel.BookListViewModel
import com.example.uasprojectmap.viewmodel.JournalListViewModel

//Container awal tabscreen Books & Journals
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BooksJournalScreen(navController: NavController) {
    //Cek current destination route atau rute screen kita berada dimana sekarang
    when(navController.currentDestination?.route){
        Screen.BookListScreen.route -> navController.navigate(Screen.BookListScreen.route) //Jika route == "book_list_screen"
        Screen.JournalListScreen.route -> navController.navigate(Screen.JournalListScreen.route) //Jika route == "journal_list_screen"
    }
}

//Component funtion untuk Top Bar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarBooksJournal(navController: NavController){
    //Variable untuk cek Options Button di klik atau tidak
    var expanded by rememberSaveable { mutableStateOf(false) }
    //Variable yang berisi pilihan untuk melihat buku atau jurnal
    val options = listOf("Books", "Journals")

    //Cek kondisi dari selectedIndex. Jika 0 mengarah ke "book_list_screen, Jika 1 mengarah ke "journal_list_screen"
    var selectedIndex = remember { mutableStateOf(0) }
    if(navController.currentDestination?.route == Screen.BookListScreen.route){
        selectedIndex.value = 0
    }
    else if(navController.currentDestination?.route == Screen.JournalListScreen.route){
        selectedIndex.value = 1
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally){
        CenterAlignedTopAppBar(
            navigationIcon = {
                //Tombol untuk kembali ke "home_screen"
                IconButton(onClick = { navController.navigate(Screen.HomeScreen.route){popUpTo("books_journal_route")} }) {
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
                    text = "Books & Journals",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.dark_heading)
                )
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

        //Button untuk lihat list Book atau Journal
        SingleChoiceSegmentedButtonRow(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)) {
            options.forEachIndexed { index, label ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size, baseShape = RoundedCornerShape(10.dp)),
                    onClick = {
                        selectedIndex.value = index //selectedIndex nilainya berubah sesuai dengan index dari tompol yang dipilih
                        navController.navigate(label.dropLast(1).lowercase() +"_list_screen")
                    },
                    selected = index == selectedIndex.value,
                    icon = {null},
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = colorResource(id = R.color.primary_main),
                        activeContentColor = Color(0xFFFCFCFA),
                        inactiveContentColor = Color(0xFF8F90A6)
                    ),
                    border = SegmentedButtonDefaults.borderStroke(color = colorResource(id = R.color.light_grey))
                ) {
                    Text(text = label)
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))


        HorizontalDivider(
            thickness = 1.dp,
            color = colorResource(id = R.color.light_grey)
        )
    }
}

//Layar untuk menampilkan list buku
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun BookListScreen(navController: NavController, bookListViewModel: BookListViewModel) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBarBooksJournal(navController = navController)}
    ){
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

                //Tampilan dari list buku yang diambil dari Firebase
                LazyVerticalGrid(
                    modifier = Modifier
                        .padding(it)
                        .padding(top = 31.dp)
                        .padding(horizontal = 20.dp),
                    columns = GridCells.Adaptive(114.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    content = {
                        items(bookListViewModel.bookList){ book -> //list buku dipanggil dari Firebase melalui ViewModel
                            //Untuk buku yang dipanggil akan ditampilkan dengan bentuk Card
                            BooksJournalsCard(publication = book){
                                navController.navigate(route = Screen.DetailBookScreen.route+"/"+book.id)
                            }
                        }
                    }
                )
            }
        }
    }
}

//Layar untuk menampilkan list jurnal
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun JournalListScreen(navController: NavController, journalListViewModel: JournalListViewModel) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBarBooksJournal(navController = navController)}
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

                //Tampilan dari list buku yang diambil dari Firebase
                LazyVerticalGrid(
                    modifier = Modifier
                        .padding(it)
                        .padding(top = 31.dp)
                        .padding(horizontal = 20.dp),
                    columns = GridCells.Adaptive(114.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    content = {
                        items(journalListViewModel.journalList){ journal -> //list jurnal dipanggil dari Firebase melalui ViewModel
                            //Untuk jurnal yang dipanggil akan ditampilkan dalam bentuk Card
                            BooksJournalsCard(publication = journal){
                                navController.navigate(route =Screen.DetailJournalScreen.route+"/"+journal.id)
                            }
                        }
                    }
                )
            }
        }
    }
}

//Component function untuk template Card dari Buku atau Jurnal
@Composable
fun BooksJournalsCard(
    publication: Publication,
    clickAction: () -> Unit
) {
    val urlImg = publication.url_img
    val title = publication.title
    val author = publication.author

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier
            .width(114.dp)
            .height(164.dp)
            .shadow(elevation = 10.dp, shape = RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .clickable(
                onClick = clickAction
            ),
            content = { AsyncImage(
                model = urlImg,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )}
        )
        Text(
            text = title,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF28293D),
            lineHeight = 10.sp,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(top = 4.dp)
                .align(alignment = Alignment.Start)
        )
        Text(
            text = author,
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF8F90A6),
            lineHeight = 10.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(top = 2.dp)
                .align(alignment = Alignment.Start)
        )
    }
}


//Preview
@Preview
@Composable
fun BooksJournalScreenPreview(){
    val navController = rememberNavController()
    BooksJournalScreen(navController = navController)
}

@Preview
@Composable
fun BookListScreenPreview(){
    val navController = rememberNavController()
    val bookListViewModel = viewModel<BookListViewModel>()
    BookListScreen(navController, bookListViewModel)
}

@Preview
@Composable
fun JournalListScreenPreview(){
    val navController = rememberNavController()
    val journalListViewModel = viewModel<JournalListViewModel>()
    JournalListScreen(navController, journalListViewModel)
}

@Preview
@Composable
fun BooksJournalsCardPreview(
) {
    val publication = Publication("-1", "TITLE", "AUTHOR", "DESCRIPTION", "null", "null")

    BooksJournalsCard(publication = publication){ null }
}