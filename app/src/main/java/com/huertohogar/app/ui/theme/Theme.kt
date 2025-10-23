package com.huertohogar.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * `LightColorScheme` define la paleta de colores para el tema claro de la aplicación.
 * Cada "slot" de color (primary, secondary, background, etc.) tiene un propósito específico
 * en los componentes de Material Design.
 */
private val LightColorScheme = lightColorScheme(
    primary = VerdeEsmeralda,       // Color principal, usado en botones, FABs y elementos importantes.
    onPrimary = Color.White,        // Color del texto y los iconos que van SOBRE el color primario.
    secondary = AmarilloMostaza,    // Color secundario para acentos y elementos menos importantes.
    onSecondary = GrisOscuro,       // Texto e iconos SOBRE el color secundario.
    tertiary = MarronClaro,         // Color terciario, para dar más variedad a la paleta.
    background = FondoBlanco,       // El color de fondo principal de las pantallas.
    onBackground = GrisOscuro,      // El color del texto que va SOBRE el fondo.
    surface = FondoTarjetas,        // El color de las superficies como tarjetas, menús, etc.
    onSurface = GrisOscuro,         // El color del texto que va SOBRE las superficies.
    error = Error,                  // Color para indicar errores.
    onError = Color.White           // Texto e iconos SOBRE el color de error.
)

/**
 * `DarkColorScheme` define la paleta de colores para cuando el usuario tiene activado el modo oscuro.
 * Se usan colores más oscuros para los fondos y más claros para el texto para asegurar el contraste.
 */
private val DarkColorScheme = darkColorScheme(
    primary = VerdeClaro,           // Un verde más claro para que resalte en el fondo oscuro.
    onPrimary = Color.Black,
    secondary = AmarilloMostaza,
    onSecondary = GrisOscuro,
    tertiary = MarronClaro,
    background = GrisOscuro,        // El fondo de la app ahora es un gris oscuro.
    onBackground = Color.White,     // El texto principal ahora es blanco para ser legible.
    surface = Color(0xFF222222),    // Un gris un poco más claro para las tarjetas.
    onSurface = Color.White,
    error = Error,
    onError = Color.White
)

/**
 * `HuertoHogarAppTheme` es el "Composable" principal que envuelve toda la aplicación.
 * Se encarga de aplicar los colores, la tipografía y las formas definidas.
 *
 * @param darkTheme Un booleano que indica si se debe usar el tema oscuro. Por defecto,
 *                  detecta automáticamente la configuración del sistema del teléfono.
 * @param content El contenido de la aplicación (las pantallas) al que se le aplicará el tema.
 */
@Composable
fun HuertoHogarAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Se elige la paleta de colores correcta (clara u oscura) según el parámetro `darkTheme`.
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    // Este bloque de código cambia el color de la barra de estado del sistema (la de arriba del todo).
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Se le asigna el color primario del tema a la barra de estado.
            window.statusBarColor = colorScheme.primary.toArgb()
            // Se le dice al sistema si los iconos de la barra de estado deben ser claros u oscuros.
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    // `MaterialTheme` es el componente de Jetpack Compose que aplica el tema.
    MaterialTheme(
        colorScheme = colorScheme,      // Aplica la paleta de colores elegida.
        typography = AppTypography,     // Aplica los estilos de texto definidos en `Type.kt`.
        content = content               // Muestra el contenido de la app con el tema ya aplicado.
    )
    // TODO: Se podría añadir soporte para "Dynamic Coloring" de Android 12+ (Material You).
    // Esto permitiría que la paleta de colores de la app se adapte automáticamente al fondo de pantalla del usuario.
}
