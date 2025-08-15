package dev.belalkhan.taletree

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.belalkhan.taletree.auth.AuthView
import dev.belalkhan.taletree.home.HomeView

sealed class AuthState {
    object Loading : AuthState()
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
}

sealed class NavDestinations(val route: String) {
    object Login : NavDestinations("login")
    object Home : NavDestinations("home")
}

@Composable
fun AppNavigator(navController: NavHostController, authState: AuthState) {

    val startDestination = when (authState) {
        AuthState.Authenticated -> NavDestinations.Home.route
        else -> NavDestinations.Login.route
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable(NavDestinations.Login.route) {
            AuthView()
        }

        composable(NavDestinations.Home.route) {
            HomeView()
        }
    }
}