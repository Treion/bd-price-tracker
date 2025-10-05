package com.bd.pricemonitor.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bd.pricemonitor.ui.SplashRoute
import com.bd.pricemonitor.ui.HomeRoute
import com.bd.pricemonitor.ui.SubcategoryRoute
import com.bd.pricemonitor.ui.ProductsRoute
import com.bd.pricemonitor.ui.ProductDetailRoute
import com.bd.pricemonitor.ui.SearchRoute
import com.bd.pricemonitor.ui.SettingsRoute

sealed class Destinations(val route: String) {
    data object Splash: Destinations("splash")
    data object Home: Destinations("home")
    data object Subcategory: Destinations("subcategory/{categoryId}")
    data object Products: Destinations("products/{subcategoryId}")
    data object ProductDetail: Destinations("product/{productId}")
    data object Search: Destinations("search")
    data object Settings: Destinations("settings")
}

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Destinations.Splash.route) {
        composable(Destinations.Splash.route) { SplashRoute(onFinished = { navController.navigate(Destinations.Home.route) { popUpTo(Destinations.Splash.route) { inclusive = true } } }) }
        composable(Destinations.Home.route) { HomeRoute(navController) }
        composable(Destinations.Subcategory.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("categoryId")?.toLongOrNull() ?: -1L
            SubcategoryRoute(navController, id)
        }
        composable(Destinations.Products.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("subcategoryId")?.toLongOrNull() ?: -1L
            ProductsRoute(navController, id)
        }
        composable(Destinations.ProductDetail.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("productId")?.toLongOrNull() ?: -1L
            ProductDetailRoute(navController, id)
        }
        composable(Destinations.Search.route) { SearchRoute(navController) }
        composable(Destinations.Settings.route) { SettingsRoute(navController) }
    }
}
