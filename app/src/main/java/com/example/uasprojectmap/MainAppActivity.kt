package com.example.uasprojectmap

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.uasprojectmap.navigation.NavGraph
import com.example.uasprojectmap.ui.theme.LearnHUBTheme
import com.google.firebase.auth.FirebaseAuth


//Template data class untuk menyimpan item icon dan nama tab di Bottom Navigation Bar
data class BottomNavigationItem(
    val title: String,
    val icon: ImageVector
)

//Jalur utama dari aplikasi berjalan setelah Login dan Register.
class MainAppActivity: ComponentActivity() {
    //navController digunakan untuk berpindah layar
    private lateinit var navController: NavHostController
    //selectedItemIndex digunakan untuk tab di BottomNavigationBar
    private var selectedItemIndex =  mutableStateOf(0)
    //Inisialisasi FirebaseAuth
    private val auth = FirebaseAuth.getInstance()

    @SuppressLint("UnusedBoxWithConstraintsScope")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LearnHUBTheme {
                //Inisialisasi navController
                navController = rememberNavController()

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold( //Untuk struktur app. Based on Material 3
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = { BottomNavigation(navController, selectedItemIndex) }
                    ) {
                        Column(modifier = Modifier.padding(it)) {
                            //NavGraph digunakan untuk berpidah tampilan di Jetpack Compose
                            NavGraph(
                                navController = navController,
                                selectedItemIndex = selectedItemIndex,
                                auth = auth,
                                exitApp = { finishAffinity() } //Membuang semua activity
                            )
                        }
                    }
                }
            }
        }
    }
}

//Component Function untuk Bottom Navigation Bar
@Composable
fun BottomNavigation(navController: NavHostController, selectedItemIndex: MutableState<Int>){
    val items = listOf( //define item yang akan dipakai untuk navbar
        BottomNavigationItem(
            title = "Home",
            icon = ImageVector.vectorResource(id = R.drawable.ic_home)
        ),
        BottomNavigationItem(
            title = "Books & Journal",
            icon = ImageVector.vectorResource(id = R.drawable.ic_book)
        ),
        BottomNavigationItem(
            title = "Account",
            icon = ImageVector.vectorResource(id = R.drawable.ic_account)
        )
    )

    Column {
        HorizontalDivider(
            thickness = 1.dp,
            color = colorResource(id = R.color.light_grey)
        )

        NavigationBar(
            containerColor = Color.White
        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = selectedItemIndex.value == index,
                    onClick = {
                        selectedItemIndex.value = index
                        if(selectedItemIndex.value == 1){
                            navController.navigate("books_journal_route")
                        }
                        else{
                            navController.navigate(item.title.lowercase()+"_screen")
                        }
                    },
                    label = {
                        Text(
                            text = item.title,
                        )
                    },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorResource(id = R.color.primary_main),
                        selectedTextColor = colorResource(id = R.color.primary_main),
                        unselectedIconColor = Color.LightGray,
                        unselectedTextColor = Color.LightGray,
                        indicatorColor = MaterialTheme.colorScheme.background
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun BottomNavigationPreview(){
    val navController = rememberNavController()
    var selectedItemIndex = mutableStateOf(0)
    BottomNavigation(navController = navController, selectedItemIndex)
}