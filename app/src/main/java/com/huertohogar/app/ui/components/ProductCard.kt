package com.huertohogar.app.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
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
import com.huertohogar.app.R
import com.huertohogar.app.model.Producto
import com.huertohogar.app.ui.theme.HuertoHogarAppTheme

/**
 * Un Composable reutilizable que muestra la información de un producto en formato de tarjeta.
 * Está diseñado para ser visualmente atractivo y para manejar la carga de imágenes de forma asíncrona.
 *
 * @param producto El objeto `Producto` que contiene los datos a mostrar.
 * @param onProductClick Una función (lambda) que se ejecutará cuando el usuario pulse sobre la tarjeta.
 *                       Recibe el ID del producto como parámetro para que quien la use sepa en qué producto se hizo clic.
 */
@SuppressLint("DefaultLocale") // Se usa para suprimir un aviso sobre el formato de Strings.
@Composable
fun ProductCard(
    producto: Producto,
    onProductClick: (String) -> Unit
) {
    // `Card` es el componente principal que le da el aspecto de tarjeta con sombra y bordes redondeados.
    Card(
        modifier = Modifier
            .widthIn(min = 160.dp, max = 200.dp) // Limita el ancho de la tarjeta.
            .padding(vertical = 4.dp, horizontal = 4.dp)
            .clickable { onProductClick(producto.id) }, // Hace que toda la tarjeta sea clickable.
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), // Le da una pequeña sombra.
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant // Usa un color del tema para el fondo.
        )
    ) {
        // `Column` apila los elementos verticalmente (la imagen encima del texto).
        Column {
            // `AsyncImage` es una maravilla de la librería Coil. Carga imágenes desde una URL.
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(producto.imagenUrl) // La URL de la imagen del producto.
                    .crossfade(true) // Le da una suave transición de fundido a la imagen.
                    .placeholder(R.drawable.ic_placeholder_image) // Imagen a mostrar MIENTRAS carga la real.
                    .error(R.drawable.ic_error_image) // Imagen a mostrar si hay un error al cargar.
                    .build(),
                contentDescription = "Imagen de ${producto.nombre}",
                modifier = Modifier
                    .fillMaxWidth() // La imagen ocupa todo el ancho de la tarjeta.
                    .aspectRatio(1f), // Mantiene la proporción cuadrada (ancho = alto).
                contentScale = ContentScale.Crop // Recorta la imagen para que llene el espacio sin deformarse.
            )

            // Otra `Column` para organizar el texto con un poco de padding.
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1 // Asegura que el nombre no ocupe más de una línea.
                )
                Spacer(modifier = Modifier.height(4.dp)) // Un pequeño espacio vertical.
                Text(
                    // Formatea el precio para que se vea como "1.200" en lugar de "1200.0".
                    text = "$${String.format("%,.0f", producto.precio)} CLP / kg",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary, // Usa el color primario del tema para destacar.
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
    // TODO: Se podría añadir un pequeño icono o un botón de "Añadir al carrito" en la esquina de la tarjeta
    // para un acceso más rápido, además del clic para ver los detalles.
    // TODO: Se podría mostrar una etiqueta de "Agotado" si el `producto.stock` es 0.
}


/**
 * `ProductCardPreview` nos permite ver cómo se ve el componente `ProductCard` directamente
 * en el editor de Android Studio sin tener que ejecutar la aplicación completa.
 * Es súper útil para hacer ajustes de diseño rápidos.
 */
@Preview(showBackground = true, widthDp = 200)
@Composable
fun ProductCardPreview() {
    // Creamos un producto de mentira para poder previsualizar la tarjeta.
    val productoDeEjemplo = Producto(
        id = "FR001",
        nombre = "Manzanas Fuji Rojas del Campo",
        descripcion = "Deliciosas manzanas.",
        precio = 1200.0,
        stock = 150,
        categoria = "frutas",
        imagenUrl = "url_invalida", // Usamos una URL inválida para probar el placeholder de error.
        origen = "Valle del Maule"
    )
    // Envolvemos el componente en nuestro tema para que la preview se vea con los colores y fuentes correctos.
    HuertoHogarAppTheme {
        ProductCard(producto = productoDeEjemplo, onProductClick = {})
    }
}
