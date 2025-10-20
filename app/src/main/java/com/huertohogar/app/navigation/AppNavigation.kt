package com.huertohogar.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.huertohogar.app.ui.screens.HomeScreen

/**
 * Este Composable gestiona todas las rutas de la aplicación.
 * Es el "controlador de tráfico" que muestra la pantalla correcta.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home_screen") {
        // Define la primera pantalla que se mostrará al abrir la app.
        composable("home_screen") {
            HomeScreen(navController)
        }

        // Aquí agregarás las otras pantallas más adelante:
        // composable("products_screen") { ProductsScreen(navController) }
        // composable("login_screen") { LoginScreen(navController) }
    }
}
