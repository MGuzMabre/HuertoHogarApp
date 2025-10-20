package com.huertohogar.app.navigation

/**
 * Define todas las rutas de navegación de la aplicación de forma segura.
 * Usar una sealed class previene errores al escribir las rutas como Strings.
 */
sealed class AppScreens(val route: String) {
    // Rutas para pantallas que no necesitan argumentos
    object HomeScreen : AppScreens("home_screen")
    object ProductsScreen : AppScreens("products_screen")
    object AboutScreen : AppScreens("about_screen") // Pantalla "Nosotros"
    object BlogScreen : AppScreens("blog_screen")
    object ContactScreen : AppScreens("contact_screen")
    object CartScreen : AppScreens("cart_screen")
    object LoginScreen : AppScreens("login_screen")
    object RegisterScreen : AppScreens("register_screen")
    object AdminScreen : AppScreens("admin_screen") // Pantalla de Administrador

    // Rutas para pantallas que SÍ necesitan argumentos
    // Por ejemplo, para ver el detalle de un producto específico

    /**
     * Ruta para la pantalla de detalle de un producto.
     * Requiere un 'productId' para saber qué producto mostrar.
     *
     * Uso: AppScreens.ProductDetailScreen.createRoute("FR001")
     */
    object ProductDetailScreen : AppScreens("product_detail_screen/{productId}") {
        fun createRoute(productId: String) = "product_detail_screen/$productId"
    }

    /**
     * Ruta para la pantalla de detalle de un artículo del blog.
     * Requiere un 'articleId' para saber qué artículo mostrar.
     */
    object BlogDetailScreen : AppScreens("blog_detail_screen/{articleId}") {
        fun createRoute(articleId: String) = "blog_detail_screen/$articleId"
    }
}
