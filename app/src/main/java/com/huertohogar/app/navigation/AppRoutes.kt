package com.huertohogar.app.navigation

/**
 * Sealed class que define todas las rutas de navegación de la aplicación de forma segura.
 * Esto evita errores de escritura al navegar, ya que el compilador verificará las rutas.
 */
sealed class AppScreens(val route: String) {
    // Pantallas principales
    object HomeScreen : AppScreens("home_screen")
    object ProductsScreen : AppScreens("products_screen")
    object CartScreen : AppScreens("cart_screen")

    // Pantallas de Autenticación
    object LoginScreen : AppScreens("login_screen")
    object RegisterScreen : AppScreens("register_screen")

    // Pantalla con argumentos (ejemplo para detalles de producto)
    object ProductDetailScreen : AppScreens("product_detail_screen/{productId}") {
        /**
         * Función auxiliar para construir la ruta completa con el ID del producto.
         * Uso: AppScreens.ProductDetailScreen.createRoute("FR001")
         */
        fun createRoute(productId: String) = "product_detail_screen/$productId"
    }
}

