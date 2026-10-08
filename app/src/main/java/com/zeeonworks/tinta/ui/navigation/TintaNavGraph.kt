package com.zeeonworks.tinta.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zeeonworks.tinta.ui.archives.ArchivesScreen
import com.zeeonworks.tinta.ui.reader.ReaderScreen

sealed class Screen(val route: String) {
    data object Archives : Screen("archives")
    data object Reader : Screen("reader/{archiveId}") {
        fun createRoute(archiveId: String): String {
            return "reader/$archiveId"
        }
    }
}

@Composable
fun TintaNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Archives.route
    ) {
        composable(Screen.Archives.route) {
            ArchivesScreen(
                onNavigateToReader = { archiveId ->
                    navController.navigate(Screen.Reader.createRoute(archiveId))
                }
            )
        }

        composable(
            route = Screen.Reader.route,
            arguments = listOf(
                navArgument("archiveId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val archiveId = backStackEntry.arguments?.getString("archiveId") ?: ""
            ReaderScreen(
                archiveId = archiveId,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
