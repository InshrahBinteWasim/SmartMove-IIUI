package com.example.smartmoveiiui.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.smartmoveiiui.model.AppRole
import com.example.smartmoveiiui.navigation.HomeDestinations
import com.example.smartmoveiiui.ui.home.AdminHomeScreen
import com.example.smartmoveiiui.ui.home.CommuterHomeScreen
import com.example.smartmoveiiui.ui.home.StaffHomeScreen
import com.example.smartmoveiiui.ui.placeholder.FeaturePlaceholderScreen

@Composable
fun SmartMoveNavHost(
    appRole: AppRole,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val startDestination = when (appRole) {
        AppRole.COMMUTER -> HomeDestinations.COMMUTER_HOME
        AppRole.TRANSPORT_STAFF -> HomeDestinations.STAFF_HOME
        AppRole.SYSTEM_ADMINISTRATOR -> HomeDestinations.ADMIN_HOME
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(HomeDestinations.COMMUTER_HOME) {
            CommuterHomeScreen(
                onNavigateToFeature = { id ->
                    navController.navigate(HomeDestinations.placeholder(id))
                },
            )
        }
        composable(HomeDestinations.STAFF_HOME) {
            StaffHomeScreen(
                onNavigateToFeature = { id ->
                    navController.navigate(HomeDestinations.placeholder(id))
                },
            )
        }
        composable(HomeDestinations.ADMIN_HOME) {
            AdminHomeScreen(
                onNavigateToFeature = { id ->
                    navController.navigate(HomeDestinations.placeholder(id))
                },
            )
        }
        composable(
            route = HomeDestinations.PLACEHOLDER,
            arguments = listOf(
                navArgument("featureId") { type = NavType.StringType },
            ),
        ) { entry ->
            val featureId = entry.arguments?.getString("featureId").orEmpty()
            FeaturePlaceholderScreen(
                featureId = featureId,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
