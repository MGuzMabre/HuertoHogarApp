package com.huertohogar.app.model

/**
 * Representa el estado completo de la UI para la pantalla del carrito de compras.
 * Contiene la lista de items, el subtotal y el total.
 */
data class CartUiState(
    val items: List<CartItem> = emptyList()
) {
    val subtotal: Double
        get() = items.sumOf { it.subtotal }


    val costoEnvio: Double
        get() = if (items.isNotEmpty()) 3500.0 else 0.0

    val total: Double
        get() = subtotal + costoEnvio


    val numeroTotalItems: Int
        get() = items.sumOf { it.cantidad }
}