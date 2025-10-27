package com.huertohogar.app.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.huertohogar.app.ui.screens.CartScreen
import com.huertohogar.app.ui.screens.HomeScreen
import com.huertohogar.app.ui.screens.LoginScreen
import com.huertohogar.app.ui.screens.ProductDetailScreen
import com.huertohogar.app.ui.screens.ProductsScreen
import com.huertohogar.app.ui.screens.ProfileScreen // Importamos la nueva pantalla
import com.huertohogar.app.ui.screens.RegisterScreen
import com.huertohogar.app.viewmodel.CartViewModel
import com.huertohogar.app.viewmodel.ProfileViewModel // Importamos el ProfileViewModel

/**
 * Composable que define el grafo de navegación principal de la aplicación.
 * Gestiona qué pantalla se muestra en cada momento.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Creamos la instancia única del CartViewModel aquí
    val cartViewModel: CartViewModel = viewModel()

    // Creamos la instancia única del ProfileViewModel aquí.
    // Al crearlo aquí (fuera del NavHost), su 'vida' (scope)
    // se ata al 'AppNavigation' y no se destruirá
    // al cambiar de pantalla.
    val profileViewModel: ProfileViewModel = viewModel()

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
            // Le pasamos también el profileViewModel a la HomeScreen
            HomeScreen(
                navController = navController,
                cartViewModel = cartViewModel,
                profileViewModel = profileViewModel // Se lo pasamos aquí
            )
        }
        composable(route = AppScreens.ProductsScreen.route) {
            ProductsScreen(navController = navController, cartViewModel = cartViewModel)
        }
        composable(
            route = AppScreens.ProductDetailScreen.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            ProductDetailScreen(
                navController = navController,
                productId = productId,
                cartViewModel = cartViewModel
            )
        }
        composable(route = AppScreens.CartScreen.route) {
            CartScreen(navController = navController, cartViewModel = cartViewModel)
        }

        // *** Define la pantalla para la ruta del Perfil ***
        composable(route = AppScreens.ProfileScreen.route) {
            // Le pasamos la instancia COMPARTIDA de profileViewModel
            // que creamos arriba.
            ProfileScreen(
                navController = navController,
                profileViewModel = profileViewModel
            )
        }
    }
}

