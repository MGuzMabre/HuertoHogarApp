package com.huertohogar.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.huertohogar.app.navigation.AppScreens
import com.huertohogar.app.ui.components.ProductCard
import com.huertohogar.app.viewmodel.CartViewModel
import com.huertohogar.app.viewmodel.ProductsViewModel

/**
 * La pantalla que muestra una cuadrícula con todos los productos disponibles.
 *
 * @param navController El controlador de navegación para moverse entre pantallas.
 * @param productsViewModel El ViewModel que proporciona la lista de productos.
 * @param cartViewModel El ViewModel que gestiona el estado del carrito de compras.
 */
@OptIn(ExperimentalMaterial3Api::class) // Anotación para usar componentes de Material 3 que aún son experimentales.
@Composable
fun ProductsScreen(
    navController: NavController,
    productsViewModel: ProductsViewModel = viewModel(),
    cartViewModel: CartViewModel
) {
    // `collectAsState` es la magia que conecta la UI con el ViewModel.
    // Cuando los datos en el ViewModel cambian, esta variable se actualiza y la pantalla se redibuja.
    val productsUiState by productsViewModel.uiState.collectAsState()
    val cartUiState by cartViewModel.uiState.collectAsState()

    // `Scaffold` es una plantilla de diseño de Material que proporciona una estructura
    // estándar para la pantalla (barra superior, contenido, etc.).
    Scaffold(
        topBar = {
            // La barra de navegación superior de la pantalla.
            TopAppBar(
                title = { Text("Todos los Productos") },
                // El icono a la izquierda de la barra, en este caso, la flecha para volver atrás.
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) { // `navigateUp` vuelve a la pantalla anterior.
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                // Personaliza los colores de la TopAppBar.
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary, // Fondo con el color primario del tema.
                    titleContentColor = MaterialTheme.colorScheme.onPrimary, // Color del texto del título.
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary, // Color del icono de navegación.
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary // Color de los iconos de acción (el carrito).
                ),
                // Los iconos de acción que aparecen a la derecha de la barra.
                actions = {
                    // Botón del carrito de compras.
                    IconButton(onClick = { navController.navigate(AppScreens.CartScreen.route) }) {
                        // `BadgedBox` es un contenedor que permite poner una "insignia" (badge) sobre otro elemento.
                        BadgedBox(
                            badge = {
                                // La insignia solo se muestra si hay al menos un item en el carrito.
                                if (cartUiState.numeroTotalItems > 0) {
                                    // Muestra el número total de items dentro de la insignia.
                                    Badge { Text("${cartUiState.numeroTotalItems}") }
                                }
                            }
                        ) {
                            Icon(
                                Icons.Filled.ShoppingCart,
                                contentDescription = "Carrito de compras"
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding -> // `innerPadding` contiene el espacio que ocupa la TopAppBar para que el contenido no se solape.
        // `LazyVerticalGrid` es una cuadrícula que se puede desplazar verticalmente.
        // Es "Lazy" porque solo renderiza los elementos que son visibles en pantalla, lo que es muy eficiente.
        LazyVerticalGrid(
            // `GridCells.Adaptive` crea columnas con un tamaño mínimo. Se ajustarán automáticamente
            // para llenar el espacio disponible. ¡Es genial para soportar diferentes tamaños de pantalla!
            columns = GridCells.Adaptive(minSize = 180.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Aplica el padding para no quedar debajo de la TopAppBar.
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),   // Espacio vertical entre las tarjetas.
            horizontalArrangement = Arrangement.spacedBy(8.dp) // Espacio horizontal entre las tarjetas.
        ) {
            // `items` es una función que recorre la lista de productos del estado.
            items(productsUiState.productos) { producto ->
                // Por cada producto en la lista, crea un componente `ProductCard`.
                ProductCard(
                    producto = producto,
                    onProductClick = { productId ->
                        // Cuando se hace clic en una tarjeta, navega a la pantalla de detalles de ese producto.
                        navController.navigate(
                            AppScreens.ProductDetailScreen.createRoute(productId)
                        )
                    }
                )
            }
        }
    }
}
