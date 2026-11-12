package com.oxvox.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.oxvox.app.ui.screens.*

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Dashboard : Screen("dashboard")
    object RemoteAssist : Screen("remote_assist")
    object ParentalControl : Screen("parental_control")
    object SystemMatrix : Screen("system_matrix")
    object DeveloperAdmin : Screen("developer_admin")
    object CoreSystem : Screen("core_system")
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
                onNavigateToRemote = { navController.navigate(Screen.RemoteAssist.route) },
                onNavigateToParental = { navController.navigate(Screen.ParentalControl.route) },
                onNavigateToMatrix = { navController.navigate(Screen.SystemMatrix.route) },
                onNavigateToDevAdmin = { navController.navigate(Screen.DeveloperAdmin.route) },
                onNavigateToCore = { navController.navigate(Screen.CoreSystem.route) }
            )
        }
        composable(Screen.RemoteAssist.route) { RemoteAssistanceScreen(onBack = { navController.popBackStack() }) }
        composable(Screen.ParentalControl.route) { ParentalControlScreen(onBack = { navController.popBackStack() }) }
        composable(Screen.SystemMatrix.route) { SystemMatrixScreen(onBack = { navController.popBackStack() }) }
        composable(Screen.CoreSystem.route) {
            CoreSystemScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.DeveloperAdmin.route) { DeveloperAdminScreen(onBack = { navController.popBackStack() }) }
    }
}
