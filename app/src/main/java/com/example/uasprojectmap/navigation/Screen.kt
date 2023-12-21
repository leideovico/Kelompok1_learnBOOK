package com.example.uasprojectmap.navigation

//Class Screen ini digunakan untuk mendata ada screen atau route apa saja yang akan dilalui di NavGraph
sealed class Screen(val route: String){
    object LoginScreen: Screen(route = "login_screen")
    object RegisterScreen: Screen(route = "register_screen")
    object HomeScreen: Screen(route = "home_screen")
    object BookListScreen : Screen(route = "book_list_screen")
    object JournalListScreen : Screen(route = "journal_list_screen")
    object DetailBookScreen : Screen(route = "detail_book_screen")
    object DetailJournalScreen : Screen(route = "detail_journal_screen")
    object AccountScreen: Screen(route = "account_screen")
}
