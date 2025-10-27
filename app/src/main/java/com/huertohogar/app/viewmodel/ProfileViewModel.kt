package com.huertohogar.app.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.huertohogar.app.data.local.datastorage.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Data class que representa el estado de la UI para la pantalla de Perfil.
 *
 * @param userEmail El email del usuario (cargado desde DataStore).
 * @param profileImageUri La URI de la imagen de perfil seleccionada o tomada. Null si no hay imagen.
 * @param isLoading Indica si se está cargando el email.
 */
data class ProfileUiState(
    val userEmail: String? = null,
    val profileImageUri: Uri? = null,
    val isLoading: Boolean = false
)

/**
 * ViewModel para la pantalla de Perfil (ProfileScreen).
 * Gestiona el estado de la UI, incluyendo la imagen de perfil
 * y la información del usuario desde DataStore.
 *
 * Hereda de AndroidViewModel para obtener el 'application' context.
 */
class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    // Instancia de SessionManager para interactuar con DataStore
    private val sessionManager = SessionManager(application)

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        // En cuanto el ViewModel se crea, mandamos a cargar el email
        loadUserEmail()
    }

    /**
     * Lee el email desde DataStore y actualiza el estado de la UI.
     * Ahora usa .collect() para "escuchar" cambios en tiempo real.
     */
    private fun loadUserEmail() {
        viewModelScope.launch {
            // Mostramos el indicador de carga
            _uiState.update { it.copy(isLoading = true) }

            // sessionManager.getUserEmail() devuelve un Flow.
            // Usamos .collect() para "escuchar" de forma continua.
            // Este bloque se ejecutará cada vez que el email en DataStore cambie.
            sessionManager.getUserEmail().collect { emailDesdeDataStore ->

                // Actualizamos el estado con el email encontrado y ocultamos la carga
                // la primera vez que recibimos un valor.
                _uiState.update { currentState ->
                    currentState.copy(
                        userEmail = emailDesdeDataStore,
                        isLoading = false
                    )
                }
            }
        }
    }

    /**
     * Actualiza la URI de la imagen de perfil en el estado.
     * @param uri La nueva URI de la imagen, o null si se cancela.
     */
    fun updateProfileImage(uri: Uri?) {
        _uiState.update { currentState ->
            currentState.copy(profileImageUri = uri)
        }
    }

    /**
     * Ejecuta la lógica de cierre de sesión.
     * Borra el email de DataStore y limpia la imagen de perfil del estado.
     */
    fun onLogout() {
        viewModelScope.launch {
            // Borra el email guardado en DataStore
            sessionManager.clearUserEmail()

            // Limpia el estado del ViewModel (email e imagen)
            // No es estrictamente necesario porque .collect() lo hará,
            // pero es una buena práctica para una respuesta inmediata de la UI.
            _uiState.update {
                it.copy(
                    userEmail = null,
                    profileImageUri = null
                )
            }
        }
    }
}

