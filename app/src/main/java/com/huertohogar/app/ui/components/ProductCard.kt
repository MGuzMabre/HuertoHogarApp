package com.huertohogar.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.huertohogar.app.model.Producto
import com.huertohogar.app.ui.theme.HuertoHogarAppTheme

/**
 * Un Composable reutilizable que muestra la información de un producto en una tarjeta.
 *
 * @param producto El objeto Producto a mostrar.
 * @param onProductClick Lambda que se ejecuta cuando se hace clic en la tarjeta.
 */
@Composable
fun ProductCard(
    producto: Producto,
    onProductClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .padding(8.dp)
            .clickable { onProductClick(producto.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            // AsyncImage carga la imagen desde la URL.
            // Es parte de la librería Coil que agregamos.
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(producto.imagenUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Imagen de ${producto.nombre}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$${"%.0f".format(producto.precio)} CLP",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

/**
 * La anotación @Preview nos permite ver cómo se ve este componente
 * en Android Studio sin necesidad de ejecutar toda la app.
 */
@Preview(showBackground = true)
@Composable
fun ProductCardPreview() {
    val productoDeEjemplo = Producto(
        id = "FR001",
        nombre = "Manzanas Fuji",
        descripcion = "Deliciosas manzanas.",
        precio = 1200.0,
        stock = 150,
        categoria = "frutas",
        imagenUrl = "", // La URL no es necesaria para el preview de la estructura
        origen = "Valle del Maule"
    )
    HuertoHogarAppTheme {
        ProductCard(producto = productoDeEjemplo, onProductClick = {})
    }
}
