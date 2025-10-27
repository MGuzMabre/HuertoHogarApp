package com.huertohogar.app.viewmodel

import androidx.lifecycle.ViewModel
import com.huertohogar.app.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel para la pantalla de inicio (HomeScreen).
 * Se encarga de la lógica de negocio y de exponer el estado a la UI.
 */
class HomeViewModel : ViewModel() {

    // _uiState es un flujo de datos que guarda el estado actual de la pantalla de inicio.
    // Es "mutable" porque su valor puede cambiar, y "privado" para que solo el ViewModel lo modifique.
    private val _uiState = MutableStateFlow(HomeUiState())

    // uiState es la versión pública y de solo lectura del estado.
    // La pantalla (UI) observa este flujo y se actualiza automáticamente cuando los datos cambian.
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    /**
     * El bloque `init` se ejecuta automáticamente cuando se crea el ViewModel por primera vez.
     * Es ideal para cargar los datos iniciales que necesita la pantalla.
     */
    init {
        cargarProductosDestacados()
    }

    /**
     * Carga una lista de productos destacados y actualiza el estado de la UI.
     * Por ahora, los productos están "hardcodeados" (escritos directamente aquí).
     */
    private fun cargarProductosDestacados() {
        // Se actualiza el valor de _uiState con una nueva instancia de HomeUiState,
        // que contiene la lista de productos que queremos mostrar.
        _uiState.value = HomeUiState(
            productosDestacados = listOf(
                Producto(
                    id = "FR001",
                    nombre = "Manzanas Fuji",
                    descripcion = "Manzanas Fuji crujientes y dulces.",
                    precio = 1200.0,
                    stock = 150,
                    categoria = "frutas",
                    imagenUrl = "https://github.com/ElMabre/ProyectoHuertoHogar/blob/main/img/manzana.jpg?raw=true",
                    origen = "Valle del Maule",
                    unidad = "Kg"
                ),
                Producto(
                    id = "VR001",
                    nombre = "Zanahorias Orgánicas",
                    descripcion = "Zanahorias crujientes cultivadas sin pesticidas.",
                    precio = 900.0,
                    stock = 100,
                    categoria = "verduras",
                    imagenUrl = "https://github.com/ElMabre/ProyectoHuertoHogar/blob/main/img/zanahoria.jpg?raw=true",
                    origen = "Región de O'Higgins",
                    unidad = "Kg"
                ),
                Producto(
                    id = "PO001",
                    nombre = "Miel Orgánica",
                    descripcion = "Miel pura y orgánica de apicultores locales.",
                    precio = 5000.0,
                    stock = 50,
                    categoria = "organicos",
                    imagenUrl = "https://github.com/ElMabre/ProyectoHuertoHogar/blob/main/img/miel.jpg?raw=true",
                    origen = "Región del Maule",
                    unidad = "Frasco 500g"
                )
            )
        )
        // TODO: En una aplicación real, estos datos deberían venir de una fuente externa,
        // como una API o una base de datos. Se debería crear una función (probablemente
        // una corrutina) para obtener estos datos de forma asíncrona y manejar
        // estados de carga (isLoading = true) y posibles errores.
    }
}

/**
 * Data class que representa el estado completo de la UI para HomeScreen.
 *
 * @param productosDestacados La lista de productos que se mostrarán en la pantalla.
 * @param isLoading Un booleano que indica si se están cargando datos (útil para mostrar un spinner).
 */
data class HomeUiState(
    val productosDestacados: List<Producto> = emptyList(),
    val isLoading: Boolean = false
)
