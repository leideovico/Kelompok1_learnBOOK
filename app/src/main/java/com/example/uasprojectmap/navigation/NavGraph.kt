package com.example.uasprojectmap.navigation

import android.content.Intent
import androidx.compose.material3.rememberBottomAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.navigation
import com.example.uasprojectmap.MainActivity
import com.example.uasprojectmap.RegisterActivity
import com.example.uasprojectmap.view.AccountScreen
import com.example.uasprojectmap.view.BookListScreen
import com.example.uasprojectmap.view.DetailScreen
import com.example.uasprojectmap.view.HomeScreen
import com.example.uasprojectmap.view.JournalListScreen
import com.example.uasprojectmap.viewmodel.AccountViewModel
import com.example.uasprojectmap.viewmodel.BookListViewModel
import com.example.uasprojectmap.viewmodel.HomeViewModel
import com.example.uasprojectmap.viewmodel.JournalListViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar

//NavGraph digunakan untuk mengontrol jalur navigasi dari aplikasi dalam sistem Jetpack Compose.
//Menggunakan Class NavHostController dan NavController
//NavGraph dipanggil di MainAppActivity.
@Composable
fun NavGraph(navController: NavHostController, selectedItemIndex: MutableState<Int>, auth: FirebaseAuth, exitApp: () -> Unit){
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route,
        route = "main_route"
    ){
        //Navigation for login
        composable(route = Screen.LoginScreen.route){
            val context = LocalContext.current
            context.startActivity(Intent(context, MainActivity::class.java))
        }

        //Navigation for go to Registration Screen
        composable(route = Screen.RegisterScreen.route){
            auth.signOut() //Current User SignOut
            val context = LocalContext.current
            context.startActivity(Intent(context, RegisterActivity::class.java))
        }

        //When pressed log out
        composable(route = Screen.LoginScreen.route+"/log_out"){
            auth.signOut() //Current User SignOut
            val context = LocalContext.current
            context.startActivity(Intent(context, MainActivity::class.java))
        }
        //When pressed exit
        composable(route = "exit_app"){
            exitApp()
        }

        //Navigate to Home Screen
        composable(route = Screen.HomeScreen.route){
            val homeViewModel = viewModel<HomeViewModel>()

            selectedItemIndex.value = 0 //index value untuk menentukan tab yang aktif di BottomNavigation
            HomeScreen(navController, homeViewModel)
        }

        //Navigate to Books & Journal Screen
        navigation(
            startDestination = Screen.BookListScreen.route,
            route = "books_journal_route"
        ){
            selectedItemIndex.value = 1  //index value untuk menentukan tab yang aktif di BottomNavigation

            composable(route = Screen.BookListScreen.route) {
                val bookListViewModel = viewModel<BookListViewModel>()
                BookListScreen(navController = navController, bookListViewModel = bookListViewModel)
            }

            //Navigate to Detail Screen for Selected Book
            composable(
                route = "${Screen.DetailBookScreen.route}/{bookId}",
                arguments = listOf(navArgument("bookId") {
                    type = NavType.StringType
                    nullable = false
                })
            ) { backStackEntry ->
                val booksViewModel = viewModel<BookListViewModel>()

                // Retrieve the bookId from the route arguments
                val bookId = backStackEntry.arguments?.getString("bookId")
                val selectedBook =
                    bookId?.let { booksViewModel.findBookById(it) }// Find the book using the bookId
                if (selectedBook != null) {
                    DetailScreen(navController = navController, selectedPublication = selectedBook)
                }
            }

            composable(route = Screen.JournalListScreen.route) {
                val journalListViewModel = viewModel<JournalListViewModel>()
                JournalListScreen(navController = navController, journalListViewModel = journalListViewModel)
            }

            //Navigate to Detail Screen for Selected Journal
            composable(
                route = "${Screen.DetailJournalScreen.route}/{journalId}",
                arguments = listOf(navArgument("journalId") {
                    type = NavType.StringType
                    nullable = false
                })
            ) { backStackEntry ->
                val journalsViewModel = viewModel<JournalListViewModel>()

                // Retrieve the journalId from the route arguments
                val journalId = backStackEntry.arguments?.getString("journalId")
                val selectedJournal =
                    journalId?.let { journalsViewModel.findJournalById(it) }// Find the journal using the journalId
                if (selectedJournal != null) {
                    DetailScreen(navController = navController, selectedPublication = selectedJournal)
                }
            }
        }

        //Navigate to Account Screen
        composable(route= Screen.AccountScreen.route){
            val accountViewModel = viewModel<AccountViewModel>()

            selectedItemIndex.value = 2  //index value untuk menentukan tab yang aktif di BottomNavigation
            AccountScreen(navController = navController, accountViewModel = accountViewModel)
        }
    }
}

