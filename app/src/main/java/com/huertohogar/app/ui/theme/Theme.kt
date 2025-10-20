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

// 1. Define la paleta de colores para el tema claro (Light Mode)
private val LightColorScheme = lightColorScheme(
    primary = VerdeEsmeralda,
    secondary = AmarilloMostaza,
    tertiary = MarronClaro,
    background = FondoBlanco,
    surface = FondoTarjetas,
    error = Error,
    onPrimary = Color.White,
    onSecondary = GrisOscuro,
    onBackground = GrisOscuro,
    onSurface = GrisOscuro,
    onError = Color.White
)

// 2. Define la paleta de colores para el tema oscuro (Dark Mode)
private val DarkColorScheme = darkColorScheme(
    primary = VerdeClaro,
    secondary = AmarilloMostaza,
    tertiary = MarronClaro,
    background = GrisOscuro,
    surface = Color(0xFF222222), // Un gris un poco más claro que el fondo
    error = Error,
    onPrimary = Color.Black,
    onSecondary = GrisOscuro,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.White
)

/**
 * El Composable principal del tema de la aplicación.
 * Envuelve todo el contenido y le aplica los colores y la tipografía definidos.
 */
@Composable
fun HuertoHogarAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography, // Asegúrate que Type.kt define esta variable
        content = content
    )
}