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

/**
 * MainActivity es la pantalla principal de la aplicación.
 * Se encarga de configurar el contenido de la interfaz de usuario.
 */
class MainActivity : ComponentActivity() {

    /**
     * onCreate se llama cuando se crea la actividad.
     * Aquí es donde se inicializa la interfaz de usuario.
     *
     * @param savedInstanceState Si la actividad se vuelve a crear, este Bundle contiene
     * los datos más recientes que se hayan guardado.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContent es una función de Jetpack Compose que define la interfaz de usuario.
        setContent {
            // HuertoHogarAppTheme es el tema de la aplicación, que define colores, fuentes, etc.
            HuertoHogarAppTheme {
                // Surface es un contenedor de Compose que representa una sección de la interfaz.
                Surface(
                    // El modifier fillMaxSize hace que el Surface ocupe toda la pantalla.
                    modifier = Modifier.fillMaxSize(),
                    // Asigna el color de fondo del tema al Surface.
                    color = MaterialTheme.colorScheme.background
                ) {
                    // AppNavigation es el componente que gestiona la navegación entre pantallas.
                    AppNavigation()
                }
            }
        }
        // TODO: Podríamos añadir aquí un "Splash Screen" para mejorar la experiencia de usuario al iniciar la app.
    }
}
