package com.example.animetest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import androidx.tv.material3.Tab
import androidx.tv.material3.TabRow
import androidx.tv.material3.Text
import com.example.animetest.presentation.screens.DetailsScreen
import com.example.animetest.presentation.screens.HomeScreen
import com.example.animetest.presentation.screens.MovieScreen
import com.example.animetest.presentation.ui.theme.AnimeTestTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            var selectedTabItem by remember { mutableStateOf<TabItem>(TabItem.HOME) }
            AnimeTestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RectangleShape
                ) {
                    Column (
                        modifier = Modifier.background(color = Color.Black)
                    ) {
                        TopBarApp(
                            modifier = Modifier.padding(vertical = 10.dp).fillMaxWidth(),
                            selectedTab = selectedTabItem,
                            {
                                if (selectedTabItem == it) null
                                else {
                                    selectedTabItem = it
                                    navController.navigate(it.route) {
                                        launchSingleTop = true
                                    }
                                }
                            })
                        NavigationHost(navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun TopBarApp(
    modifier: Modifier = Modifier,
    selectedTab: TabItem,
    onFocusChange: (TabItem) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTab.ordinal,
        modifier = modifier
            .focusRestorer()
    ) {
        TabItem.entries.forEach { tab ->
            Tab(
                selected = selectedTab == tab,
                onFocus = { onFocusChange(tab) },
            ) {
                Text(
                    text = tab.title,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }

        }
    }
}


@Composable
fun NavigationHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = RouteScreens.HomeScreen) {
        composable<RouteScreens.HomeScreen> {
            HomeScreen(onNavigateToDetails = { animeId ->
                navController.navigate(RouteScreens.DetailsScreen(animeId = animeId))
            })
        }
        composable<RouteScreens.MovieScreen> {
            MovieScreen()
        }
        composable<RouteScreens.DetailsScreen> {
            val args = it.toRoute<RouteScreens.DetailsScreen>()
            DetailsScreen(animeId = args.animeId)
        }
    }
}

enum class TabItem(
    val title: String,
    val route: RouteScreens,
//    val icon: ImageVector? = null
) {
    HOME("Home", RouteScreens.HomeScreen),
    MOVIE("Filmy", RouteScreens.MovieScreen)

}

sealed interface RouteScreens {
    @Serializable
    object HomeScreen : RouteScreens

    @Serializable
    object MovieScreen : RouteScreens

    @Serializable
    data class DetailsScreen(val animeId: Int) : RouteScreens

//    companion object {
//        val tabScreens = listOf(
//            "HomeScreen",
//            "DetailsScreen"
//        )
//    }
//    @Serializable
//    enum class Simple : AppDestination {
//        Home, Settings, Profile
//    }
}






