package com.huertohogar.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huertohogar.app.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Data class que representa el estado de la interfaz de usuario para la pantalla de detalles del producto.
 *
 * @param producto El objeto Producto que se está mostrando. Puede ser nulo si no se ha cargado.
 * @param isLoading `true` si se están cargando los datos, `false` en caso contrario.
 * @param error Un mensaje de error si algo salió mal, o nulo si no hay errores.
 */
data class ProductDetailUiState(
    val producto: Producto? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

/**
 * ViewModel para la pantalla de detalles de un producto (ProductDetailScreen).
 * Se encarga de la lógica para cargar y mostrar la información de un producto específico.
 */
class ProductDetailViewModel : ViewModel() {

    // _uiState es un flujo de datos mutable que contiene el estado actual de la pantalla.
    // Es privado para que solo este ViewModel pueda modificarlo.
    private val _uiState = MutableStateFlow(ProductDetailUiState())
    // uiState es la versión pública y de solo lectura de _uiState.
    // La pantalla (UI) observa este flujo para actualizarse cuando el estado cambia.
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    // Lista temporal de todos los productos. En una app real, esto vendría de una base de datos o una API.
    // TODO: Reemplazar esto con una llamada a un Repository cuando Room (base de datos) esté implementado.
    private val allProductsList = listOf(
        // Frutas
        Producto("FR001", "Manzanas Fuji", "Manzanas Fuji crujientes y dulces.", 1200.0, 150, "frutas",
            "https://github.com/ElMabre/ProyectoHuertoHogar/blob/main/img/manzana.jpg?raw=true", "Valle del Maule", unidad = "Kg"),
        Producto("FR002", "Naranjas Valencia", "Jugosas y ricas en vitamina C.", 1000.0, 200, "frutas",
            "https://github.com/ElMabre/ProyectoHuertoHogar/blob/main/img/naranja.jpg?raw=true", "Región de Valparaíso",unidad = "Kg"),
        Producto("FR003", "Plátanos Cavendish", "Plátanos maduros y dulces.", 800.0, 250, "frutas",
            "https://github.com/ElMabre/ProyectoHuertoHogar/blob/main/img/platano.jpg?raw=true", "Región de O'Higgins",unidad = "Kg" ),
        // Verduras
        Producto("VR001", "Zanahorias Orgánicas", "Zanahorias crujientes sin pesticidas.", 900.0, 100, "verduras",
            "https://github.com/ElMabre/ProyectoHuertoHogar/blob/main/img/zanahoria.jpg?raw=true", "Región de O'Higgins", unidad = "Kg"),
        Producto("VR002", "Espinacas Frescas", "Espinacas frescas y nutritivas.", 700.0, 80, "verduras",
            "https://github.com/ElMabre/ProyectoHuertoHogar/blob/main/img/espinaca.jpg?raw=true", "Región Metropolitana", unidad = "Bolsa 500g"),
        Producto("VR003", "Pimientos Tricolores", "Pimientos rojos, amarillos y verdes.", 1500.0, 120, "verduras",
            "https://github.com/ElMabre/ProyectoHuertoHogar/blob/main/img/pimiento.jpg?raw=true", "Región de Valparaíso", unidad = "Kg"),
        // Orgánicos
        Producto("PO001", "Miel Orgánica", "Miel pura y orgánica local.", 5000.0, 50, "organicos",
            "https://github.com/ElMabre/ProyectoHuertoHogar/blob/main/img/miel.jpg?raw=true", "Región del Maule",unidad = "Frasco 500g"),
        Producto("PO002", "Quinua Orgánica", "Quinua orgánica de alta calidad.", 3500.0, 75, "organicos",
            "https://github.com/ElMabre/ProyectoHuertoHogar/blob/main/img/quinua.jpg?raw=true", "Región de La Araucanía", unidad = "Kg"),
        // Lácteos
        Producto("PL001", "Leche Entera", "Leche entera fresca de praderas.", 1200.0, 60, "lacteos",
            "https://github.com/ElMabre/ProyectoHuertoHogar/blob/main/img/leche.jpg?raw=true", "Región de Los Lagos",unidad = "L")
    )

    /**
     * Carga los detalles de un producto específico basado en su ID.
     * @param productId El ID del producto a cargar.
     */
    fun loadProductDetails(productId: String?) {
        // Primero, se verifica si el ID del producto es nulo o inválido.
        if (productId == null) {
            _uiState.value = ProductDetailUiState(error = "ID de producto inválido", isLoading = false)
            return // Se detiene la ejecución de la función si no hay ID.
        }

        // Se inicia una corrutina en el ámbito del ViewModel para no bloquear el hilo principal.
        viewModelScope.launch {
            // Se actualiza el estado para mostrar que la carga ha comenzado.
            _uiState.value = ProductDetailUiState(isLoading = true)

            // Se busca el producto en la lista `allProductsList` que coincida con el ID.
            val product = allProductsList.find { it.id == productId }

            // Si se encontró el producto...
            if (product != null) {
                // ...se actualiza el estado con el producto encontrado y se indica que la carga terminó.
                _uiState.value = ProductDetailUiState(producto = product, isLoading = false)
            } else {
                // Si no se encontró, se actualiza el estado con un mensaje de error.
                _uiState.value = ProductDetailUiState(error = "Producto no encontrado", isLoading = false)
            }
        }
    }

    // TODO: Añadir aquí la lógica para "Añadir al carrito" más adelante.
    // Esto podría implicar llamar a un caso de uso o repositorio para gestionar el estado del carrito.
}
