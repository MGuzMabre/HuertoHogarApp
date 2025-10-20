package com.huertohogar.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.huertohogar.app.ui.screens.HomeScreen
import com.huertohogar.app.ui.screens.LoginScreen
import com.huertohogar.app.ui.screens.RegisterScreen

/**
 * Composable que define el grafo de navegación principal de la aplicación.
 * Gestiona qué pantalla se muestra en cada momento.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        // La aplicación ahora comenzará en la pantalla de Login.
        startDestination = AppScreens.LoginScreen.route
    ) {
        // Define la pantalla para la ruta de Login
        composable(route = AppScreens.LoginScreen.route) {
            LoginScreen(navController = navController)
        }

        // Define la pantalla para la ruta de Registro
        composable(route = AppScreens.RegisterScreen.route) {
            RegisterScreen(navController = navController)
        }

        // Define la pantalla para la ruta de Inicio (Home)
        composable(route = AppScreens.HomeScreen.route) {
            HomeScreen(navController = navController)
        }

        // Aquí se agregarán las demás pantallas (Productos, Carrito, etc.)
    }
}

