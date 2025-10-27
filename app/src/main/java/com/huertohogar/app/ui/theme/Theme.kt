package com.huertohogar.app.ui.theme

// Mantenemos los imports necesarios
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Las definiciones de LightColorScheme y DarkColorScheme siguen igual...
private val LightColorScheme = lightColorScheme(
    primary = VerdeEsmeralda,
    onPrimary = Color.White,
    secondary = AmarilloMostaza,
    onSecondary = GrisOscuro,
    tertiary = MarronClaro,
    background = FondoBlanco,
    onBackground = GrisOscuro,
    surface = FondoTarjetas,
    onSurface = GrisOscuro,
    error = Error,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = VerdeClaro,
    onPrimary = Color.Black,
    secondary = AmarilloMostaza,
    onSecondary = GrisOscuro,
    tertiary = MarronClaro,
    background = GrisOscuro,
    onBackground = Color.White,
    surface = Color(0xFF222222),
    onSurface = Color.White,
    error = Error,
    onError = Color.White
)

@Composable
fun HuertoHogarAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    // --- ¡ARREGLO! ---
    // Quitamos TODO el bloque SideEffect que modificaba la statusBarColor.
    // Con enableEdgeToEdge(), el sistema maneja la transparencia y los colores
    // de forma más automática y moderna. El color de fondo detrás de la barra
    // ahora será el 'background' o 'surface' del tema, dependiendo del Composable.
    /*
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb() // <-- Ya no es necesario
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme // <-- Tampoco
        }
    }
    */

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
