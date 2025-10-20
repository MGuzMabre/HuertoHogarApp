package com.huertohogar.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.huertohogar.app.navigation.AppNavigation
import com.huertohogar.app.ui.theme.HuertoHogarAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 1. Aplicamos el tema personalizado que definimos (colores y fuentes)
            HuertoHogarAppTheme {
                // 2. Surface es un contenedor que aplica el color de fondo del tema.
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 3. Llamamos al gestor de navegación. Este será el punto de entrada
                    // que decidirá qué pantalla se muestra.
                    AppNavigation()
                }
            }
        }
    }
}
