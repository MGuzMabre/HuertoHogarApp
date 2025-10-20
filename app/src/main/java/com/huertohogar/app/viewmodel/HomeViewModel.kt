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

    // _uiState es privado y mutable, solo el ViewModel puede cambiarlo.
    private val _uiState = MutableStateFlow(HomeUiState())
    // uiState es público e inmutable, la UI solo puede leerlo.
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        // Carga los productos destacados cuando el ViewModel se crea.
        cargarProductosDestacados()
    }

    private fun cargarProductosDestacados() {
        // Simulación de carga de datos (como en tu productManager.js)
        _uiState.value = HomeUiState(
            productosDestacados = listOf(
                Producto("FR001", "Manzanas Fuji", "Manzanas Fuji crujientes y dulces.", 1200.0, 150, "frutas", "https://images.unsplash.com/photo-1568702846914-96b305d2aaeb?q=80&w=2070", "Valle del Maule"),
                Producto("VR001", "Zanahorias Orgánicas", "Zanahorias crujientes cultivadas sin pesticidas.", 900.0, 100, "verduras", "https://images.unsplash.com/photo-1590432337362-205a1b3a1a36?q=80&w=2070", "Región de O'Higgins"),
                Producto("PO001", "Miel Orgánica", "Miel pura y orgánica de apicultores locales.", 5000.0, 50, "organicos", "https://images.unsplash.com/photo-1558642452-9d2a7deb7f62?q=80&w=1974", "Región del Maule")
            )
        )
    }
}

/**
 * Data class que representa el estado completo de la UI para HomeScreen.
 */
data class HomeUiState(
    val productosDestacados: List<Producto> = emptyList(),
    val isLoading: Boolean = false
)
