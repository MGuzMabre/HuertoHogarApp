package com.huertohogar.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.huertohogar.app.navigation.AppScreens
import com.huertohogar.app.ui.components.ProductCard
import com.huertohogar.app.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel() // 1. Inyecta el ViewModel
) {
    // 2. Observa el estado de la UI desde el ViewModel
    val uiState by homeViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HuertoHogar") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
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
            // Sección de Bienvenida
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

            // Sección de Productos Destacados
            Text(
                text = "Productos Destacados",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // 3. Muestra una fila horizontal de productos usando LazyRow y ProductCard
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.productosDestacados) { producto ->
                    ProductCard(
                        producto = producto,
                        onProductClick = { productId ->
                            // Navega a la pantalla de detalle cuando se hace clic en un producto
                            navController.navigate(
                                AppScreens.ProductDetailScreen.createRoute(productId)
                            )
                        }
                    )
                }
            }

            // Spacer con weight para empujar el botón al fondo
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { navController.navigate(AppScreens.ProductsScreen.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Todos los Productos")
            }
        }
    }
}

