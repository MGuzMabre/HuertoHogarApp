package com.huertohogar.app.viewmodel

import androidx.lifecycle.ViewModel
import com.huertohogar.app.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel para la pantalla de listado de productos (ProductsScreen).
 * Se encarga de obtener y exponer la lista completa de productos.
 */
class ProductsViewModel : ViewModel() {

    // _uiState es un flujo de datos mutable que contiene el estado actual de la pantalla.
    // Es privado para asegurar que solo el ViewModel pueda modificarlo.
    private val _uiState = MutableStateFlow(ProductsUiState())

    // uiState es la versión pública y de solo lectura de _uiState.
    // La pantalla (UI) observa este flujo para pintarse a sí misma según los cambios.
    val uiState: StateFlow<ProductsUiState> = _uiState.asStateFlow()

    /**
     * El bloque init se ejecuta en cuanto se crea una instancia del ViewModel.
     * Es el lugar perfecto para iniciar la carga de datos inicial.
     */
    init {
        cargarTodosLosProductos()
    }

    /**
     * Carga la lista de productos y actualiza el estado de la UI (_uiState).
     * Actualmente, los productos están "hardcodeados" (escritos directamente en el código).
     */
    private fun cargarTodosLosProductos() {
        // Se crea un nuevo estado con la lista de productos y se emite al _uiState.
        _uiState.value = ProductsUiState(
            productos = listOf(
                // Frutas
                Producto("FR001", "Manzanas Fuji", "Manzanas Fuji crujientes y dulces.", 1200.0, 150, "frutas",
                    "https://github.com/ElMabre/ProyectoHuertoHogar/blob/main/img/manzana.jpg?raw=true", "Valle del Maule"),
                Producto("FR002", "Naranjas Valencia", "Jugosas y ricas en vitamina C.", 1000.0, 200, "frutas",
                    "https://github.com/ElMabre/ProyectoHuertoHogar/blob/main/img/naranja.jpg?raw=true", "Región de Valparaíso"),
                Producto("FR003", "Plátanos Cavendish", "Plátanos maduros y dulces.", 800.0, 250, "frutas",
                    "https://github.com/ElMabre/ProyectoHuertoHogar/blob/main/img/platano.jpg?raw=true", "Región de O'Higgins"),
                // Verduras
                Producto("VR001", "Zanahorias Orgánicas", "Zanahorias crujientes sin pesticidas.", 900.0, 100, "verduras",
                    "https://github.com/ElMabre/ProyectoHuertoHogar/blob/main/img/zanahoria.jpg?raw=true", "Región de O'Higgins"),
                Producto("VR002", "Espinacas Frescas", "Espinacas frescas y nutritivas.", 700.0, 80, "verduras",
                    "https://github.com/ElMabre/ProyectoHuertoHogar/blob/main/img/espinaca.jpg?raw=true", "Región Metropolitana"),
                Producto("VR003", "Pimientos Tricolores", "Pimientos rojos, amarillos y verdes.", 1500.0, 120, "verduras",
                    "https://github.com/ElMabre/ProyectoHuertoHogar/blob/main/img/pimiento.jpg?raw=true", "Región de Valparaíso"),
                // Orgánicos
                Producto("PO001", "Miel Orgánica", "Miel pura y orgánica local.", 5000.0, 50, "organicos",
                    "https://github.com/ElMabre/ProyectoHuertoHogar/blob/main/img/miel.jpg?raw=true", "Región del Maule"),
                Producto("PO002", "Quinua Orgánica", "Quinua orgánica de alta calidad.", 3500.0, 75, "organicos",
                    "https://github.com/ElMabre/ProyectoHuertoHogar/blob/main/img/quinua.jpg?raw=true", "Región de La Araucanía"),
                // Lácteos
                Producto("PL001", "Leche Entera", "Leche entera fresca de praderas.", 1200.0, 60, "lacteos",
                    "https://github.com/ElMabre/ProyectoHuertoHogar/blob/main/img/leche.jpg?raw=true", "Región de Los Lagos")
            )
        )
        // TODO: En una app real, estos datos deberían venir de una fuente externa, como una API o una base de datos local.
        // Se podría crear una función asíncrona (con corrutinas) para cargar los datos sin bloquear la UI.
        // También se podría manejar un estado de "cargando" (isLoading = true) mientras se obtienen los datos.
    }
}

/**
 * Data class que representa el estado de la UI para la pantalla de Productos.
 *
 * @param productos La lista de productos a mostrar.
 * @param isLoading `true` si se están cargando los datos, `false` en caso contrario.
 * @param searchQuery El texto de búsqueda introducido por el usuario.
 * @param selectedCategory La categoría de productos seleccionada para filtrar.
 */
data class ProductsUiState(
    val productos: List<Producto> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val selectedCategory: String? = null
)
