package com.huertohogar.app.viewmodel

import androidx.lifecycle.ViewModel
import com.huertohogar.app.model.LoginErrorState
import com.huertohogar.app.model.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel para la pantalla de Login (LoginScreen).
 * Contiene la lógica de negocio y el estado del formulario de inicio de sesión.
 */
class LoginViewModel : ViewModel() {

    // Flujo de estado privado y mutable.
    private val _uiState = MutableStateFlow(LoginUiState())
    // Flujo de estado público e inmutable para la UI.
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // --- Funciones para manejar eventos de la UI ---

    fun onEmailChange(email: String) {
        _uiState.update { currentState ->
            currentState.copy(
                email = email,
                errors = currentState.errors.copy(email = null) // Limpia el error al escribir
            )
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { currentState ->
            currentState.copy(
                password = password,
                errors = currentState.errors.copy(password = null) // Limpia el error al escribir
            )
        }
    }

    fun onTogglePasswordVisibility() {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    /**
     * Valida el formulario de login.
     * @return `true` si es válido, `false` si contiene errores.
     */
    fun validarFormulario(): Boolean {
        val state = _uiState.value
        val newErrors = LoginErrorState(
            email = if (!isValidEmail(state.email)) "El correo no es válido" else null,
            password = if (state.password.isBlank()) "La contraseña es obligatoria" else null
        )

        _uiState.update { it.copy(errors = newErrors) }

        return newErrors.email == null && newErrors.password == null
    }

    // --- Funciones de validación privadas ---

    private fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}