package com.huertohogar.app.navigation

/**
 * Sealed class que define todas las rutas de navegación de la aplicación de forma segura.
 */
sealed class AppScreens(val route: String) {
    // Pantallas principales
    object HomeScreen : AppScreens("home_screen")
    object ProductsScreen : AppScreens("products_screen")
    object CartScreen : AppScreens("cart_screen")
    object ProfileScreen : AppScreens("profile_screen") // <-- NUEVA RUTA AÑADIDA

    // Pantallas de Autenticación
    object LoginScreen : AppScreens("login_screen")
    object RegisterScreen : AppScreens("register_screen")

    // Pantalla con argumentos
    object ProductDetailScreen : AppScreens("product_detail_screen/{productId}") {
        fun createRoute(productId: String) = "product_detail_screen/$productId"
    }
}