package com.huertohogar.app.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Este archivo centraliza todos los colores que se usarán en la aplicación.
 * Definir los colores aquí como constantes ayuda a mantener la consistencia
 * y facilita los cambios en el futuro. Si quieres cambiar un color, solo
 * lo cambias en este archivo y se actualizará en toda la app.
 */

// --- Paleta de Colores Principal ---

// Un verde esmeralda vibrante, probablemente usado como el color principal de la marca (botones, acentos, etc.).
val VerdeEsmeralda = Color(0xFF00A878)

// Un verde más claro, posiblemente para el modo oscuro o para estados de "hover" o "pressed".
val VerdeClaro = Color(0xFF8DECB4)

// Un amarillo mostaza, ideal como color secundario para contrastar con el verde.
val AmarilloMostaza = Color(0xFFFFD700)

// Un marrón claro y suave, usado como color terciario para elementos menos importantes o fondos.
val MarronClaro = Color(0xFFD3A993)

// Un rojo para indicar errores, alertas o acciones destructivas (como "eliminar").
val Error = Color(0xFFB00020)


// --- Paleta de Neutros ---

// Un blanco puro, generalmente usado para el fondo principal en el tema claro.
val FondoBlanco = Color(0xFFFFFFFF)

// Un blanco ligeramente grisáceo, perfecto para el fondo de tarjetas o secciones para que destaquen sobre el fondo principal.
val FondoTarjetas = Color(0xFFF5F5F5)

// Un gris oscuro, usado comúnmente para el texto principal para asegurar una buena legibilidad.
val GrisOscuro = Color(0xFF333333)

// TODO: Se podría añadir una sección de colores específicos para el tema oscuro si la paleta
// necesitara más variaciones además de las ya definidas. Por ejemplo, un gris casi negro
// para el fondo del tema oscuro (ej: `val DarkBackground = Color(0xFF121212)`).
