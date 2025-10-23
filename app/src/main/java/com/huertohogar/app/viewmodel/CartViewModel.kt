package com.huertohogar.app.viewmodel

import androidx.lifecycle.ViewModel
import com.huertohogar.app.model.CartItem
import com.huertohogar.app.model.CartUiState
import com.huertohogar.app.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel para gestionar el estado y la lógica del carrito de compras.
 * Se encarga de añadir, eliminar, y actualizar los productos del carrito.
 */
class CartViewModel : ViewModel() {

    // _uiState es el flujo de datos interno y mutable que contiene el estado del carrito.
    // Es privado para que solo el ViewModel pueda modificarlo directamente.
    private val _uiState = MutableStateFlow(CartUiState())

    // uiState es la versión pública y de solo lectura del estado.
    // La UI observa este flujo para reaccionar a cualquier cambio en el carrito (ej: repintar la lista).
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    /**
     * Añade un producto al carrito. Si el producto ya existe, simplemente incrementa su cantidad.
     * @param producto El objeto `Producto` que se va a añadir.
     */
    fun addToCart(producto: Producto) {
        // `update` modifica el estado de forma segura y atómica.
        _uiState.update { currentState ->
            // Creamos una copia mutable de la lista de items actual para poder modificarla.
            val items = currentState.items.toMutableList()
            // Buscamos si el producto ya está en el carrito comparando su ID.
            val existingItemIndex = items.indexOfFirst { it.producto.id == producto.id }

            // Si `existingItemIndex` es -1, el producto no estaba en el carrito.
            if (existingItemIndex != -1) {
                // Si el producto ya existe, actualizamos su cantidad.
                val existingItem = items[existingItemIndex]
                // TODO: Añadir validación de stock aquí si es necesario. (Ej: no añadir más de lo disponible).
                items[existingItemIndex] = existingItem.copy(cantidad = existingItem.cantidad + 1)
            } else {
                // Si es un producto nuevo, lo añadimos a la lista con cantidad 1.
                items.add(CartItem(producto = producto, cantidad = 1))
            }
            // Devolvemos una nueva copia del estado con la lista de items actualizada.
            currentState.copy(items = items)
        }
        // TODO: Mostrar notificación de "Producto añadido" (Snackbar o Toast) para dar feedback al usuario.
    }

    /**
     * Elimina un producto completamente del carrito, sin importar su cantidad.
     * @param productId El ID del producto a eliminar.
     */
    fun removeFromCart(productId: String) {
        _uiState.update { currentState ->
            // `filterNot` crea una nueva lista excluyendo el item que coincida con el productId.
            val items = currentState.items.filterNot { it.producto.id == productId }
            currentState.copy(items = items)
        }
        // TODO: Mostrar notificación de "Producto eliminado".
    }

    /**
     * Actualiza la cantidad de un producto específico en el carrito.
     * Si la nueva cantidad es 0 o menos, elimina el producto.
     * @param productId El ID del producto a actualizar.
     * @param newQuantity La nueva cantidad deseada.
     */
    fun updateQuantity(productId: String, newQuantity: Int) {
        // Si la cantidad es 0 o negativa, es más fácil llamar a la función de eliminar.
        if (newQuantity <= 0) {
            removeFromCart(productId)
            return // Salimos de la función para no ejecutar el resto del código.
        }

        _uiState.update { currentState ->
            val items = currentState.items.toMutableList()
            val itemIndex = items.indexOfFirst { it.producto.id == productId }

            if (itemIndex != -1) {
                // TODO: Añadir validación de stock aquí si es necesario. (Ej: no permitir una cantidad mayor al stock).
                // Si encontramos el producto, actualizamos su cantidad.
                items[itemIndex] = items[itemIndex].copy(cantidad = newQuantity)
            }
            // Devolvemos el estado actualizado.
            currentState.copy(items = items)
        }
    }

    /**
     * Vacía completamente el carrito de compras, eliminando todos los productos.
     */
    fun clearCart() {
        // Simplemente reemplazamos el estado actual por uno nuevo y vacío.
        _uiState.update { CartUiState() }
        // TODO: Mostrar notificación de "Carrito vaciado".
        // TODO: Podríamos querer guardar el estado del carrito en el dispositivo (usando SharedPreferences o Room)
        // para que no se pierda si el usuario cierra la app.
    }
}
