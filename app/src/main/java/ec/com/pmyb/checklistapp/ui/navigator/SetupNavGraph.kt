package ec.com.pmyb.checklistapp.ui.navigator

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ec.com.pmyb.checklistapp.ui.screen.splashscreen.SplashScreen
import ec.com.pmyb.checklistapp.ui.screen.home.TasksScreen
import ec.com.pmyb.checklistapp.ui.viewmodel.TasksViewModel

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    taskViewModel: TasksViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(
            route = Screen.Home.route,
        ) {
            TasksScreen(navController = navController, taskViewModel)
        }

    }
}