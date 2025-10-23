package com.huertohogar.app.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.huertohogar.app.ui.screens.CartScreen // Importa la nueva pantalla
import com.huertohogar.app.ui.screens.HomeScreen
import com.huertohogar.app.ui.screens.LoginScreen
import com.huertohogar.app.ui.screens.ProductDetailScreen
import com.huertohogar.app.ui.screens.ProductsScreen
import com.huertohogar.app.ui.screens.RegisterScreen
import com.huertohogar.app.viewmodel.CartViewModel

/**
 * Composable que define el grafo de navegación principal de la aplicación.
 * Gestiona qué pantalla se muestra en cada momento.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    // Creamos la instancia única del CartViewModel aquí
    val cartViewModel: CartViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = AppScreens.LoginScreen.route
    ) {
        // --- Pantallas de Autenticación ---
        composable(route = AppScreens.LoginScreen.route) {
            LoginScreen(navController = navController)
        }
        composable(route = AppScreens.RegisterScreen.route) {
            RegisterScreen(navController = navController)
        }

        // --- Pantallas Principales ---
        composable(route = AppScreens.HomeScreen.route) {
            // Pasamos la instancia compartida de cartViewModel
            HomeScreen(navController = navController, cartViewModel = cartViewModel)
        }
        composable(route = AppScreens.ProductsScreen.route) {
            // Pasamos la instancia compartida de cartViewModel
            ProductsScreen(navController = navController, cartViewModel = cartViewModel)
        }
        composable(
            route = AppScreens.ProductDetailScreen.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            // Pasamos la instancia compartida de cartViewModel
            ProductDetailScreen(
                navController = navController,
                productId = productId,
                cartViewModel = cartViewModel
            )
        }

        // *** NUEVO: Define la pantalla para la ruta del Carrito ***
        composable(route = AppScreens.CartScreen.route) {
            // Pasamos la instancia compartida de cartViewModel
            CartScreen(navController = navController, cartViewModel = cartViewModel)
        }
    }
}