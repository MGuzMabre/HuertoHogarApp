package com.huertohogar.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape // Importado para la foto
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip // Importado para la foto
import androidx.compose.ui.layout.ContentScale // Importado para la foto
import androidx.compose.ui.platform.LocalContext // Importado para la foto
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage // Importado para la foto
import coil.request.ImageRequest // Importado para la foto
import com.huertohogar.app.navigation.AppScreens
import com.huertohogar.app.ui.components.ProductCard
import com.huertohogar.app.viewmodel.CartViewModel
import com.huertohogar.app.viewmodel.HomeViewModel
import com.huertohogar.app.viewmodel.ProfileViewModel // Importamos el ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel(),
    cartViewModel: CartViewModel,
    profileViewModel: ProfileViewModel // Recibimos el ViewModel compartido
) {
    val homeUiState by homeViewModel.uiState.collectAsState()
    val cartUiState by cartViewModel.uiState.collectAsState()

    // Leemos el estado del ProfileViewModel
    val profileUiState by profileViewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HuertoHogar") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    // Ícono del Carrito (sin cambios)
                    IconButton(onClick = { navController.navigate(AppScreens.CartScreen.route) }) {
                        BadgedBox(
                            badge = {
                                if (cartUiState.numeroTotalItems > 0) {
                                    Badge { Text("${cartUiState.numeroTotalItems}") }
                                }
                            }
                        ) {
                            Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrito")
                        }
                    }

                    // --- Ícono de Perfil (MODIFICADO) ---
                    IconButton(onClick = { navController.navigate(AppScreens.ProfileScreen.route) }) {

                        // Si la URI de la imagen en el profileUiState NO es nula,
                        // mostramos la imagen. Si ES nula, mostramos el ícono de siempre.

                        if (profileUiState.profileImageUri == null) {
                            // No hay imagen, mostramos el ícono por defecto
                            Icon(
                                imageVector = Icons.Filled.AccountCircle,
                                contentDescription = "Mi Perfil"
                            )
                        } else {
                            // ¡Hay imagen! La mostramos con Coil
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(profileUiState.profileImageUri)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Mi Perfil",
                                modifier = Modifier
                                    .size(32.dp) // Un tamaño bueno para la barra
                                    .clip(CircleShape), // La hacemos redondita
                                contentScale = ContentScale.Crop // Recortamos para que encaje
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "¡Bienvenido a HuertoHogar!",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Productos frescos y naturales directo del campo a tu hogar.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Productos Destacados",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(homeUiState.productosDestacados) { producto ->
                    ProductCard(
                        producto = producto,
                        onProductClick = { productId ->
                            navController.navigate(
                                AppScreens.ProductDetailScreen.createRoute(productId)
                            )
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { navController.navigate(AppScreens.ProductsScreen.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Ver Todos los Productos")
            }
        }
    }
}
