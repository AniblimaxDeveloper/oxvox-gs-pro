package com.oxvox.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.oxvox.app.ui.screens.LoginScreen
import com.oxvox.app.ui.screens.RegisterScreen
import com.oxvox.app.ui.screens.DashboardScreen
import com.oxvox.app.ui.screens.RemoteAssistanceScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Dashboard : Screen("dashboard")
    object RemoteAssist : Screen("remote_assist")
}

@Composable
fun OXVOXNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { navController.navigate(Screen.Dashboard.route) { popUpTo(Screen.Login.route) { inclusive = true } } },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate(Screen.Login.route) },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToRemote = { navController.navigate(Screen.RemoteAssist.route) }
            )
        }
        composable(Screen.RemoteAssist.route) {
            RemoteAssistanceScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
