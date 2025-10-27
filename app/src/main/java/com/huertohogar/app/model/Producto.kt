package com.huertohogar.app.model

/**
 * Representa la estructura de datos de un producto en la tienda.
 * Esta data class es el "Model" en nuestra arquitectura MVVM.
 */
data class Producto(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val stock: Int,
    val categoria: String,
    val imagenUrl: String,
    val origen: String,
    val unidad: String
)
