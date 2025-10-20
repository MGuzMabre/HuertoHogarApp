package com.huertohogar.app.viewmodel

import androidx.lifecycle.ViewModel
import com.huertohogar.app.model.RegisterErrorState
import com.huertohogar.app.model.RegisterUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel para la pantalla de Registro (RegisterScreen).
 * Contiene toda la lógica de negocio y el estado del formulario de registro.
 */
class RegisterViewModel : ViewModel() {

    // Flujo de estado privado y mutable que contiene el estado actual del formulario.
    private val _uiState = MutableStateFlow(RegisterUiState())
    // Flujo de estado público e inmutable, expuesto a la UI para que lo observe.
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    // --- Funciones para manejar eventos de la UI ---

    fun onNombreChange(nombre: String) {
        _uiState.update { currentState ->
            currentState.copy(
                nombre = nombre,
                errors = currentState.errors.copy(nombre = null) // Limpia el error al escribir
            )
        }
    }

    fun onApellidoChange(apellido: String) {
        _uiState.update { it.copy(apellido = apellido, errors = it.errors.copy(apellido = null)) }
    }

    fun onRunChange(run: String) {
        _uiState.update { it.copy(run = run, errors = it.errors.copy(run = null)) }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, errors = it.errors.copy(email = null)) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, errors = it.errors.copy(password = null)) }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword, errors = it.errors.copy(confirmPassword = null)) }
    }

    fun onAceptaTerminosChange(acepta: Boolean) {
        _uiState.update { it.copy(aceptaTerminos = acepta, errors = it.errors.copy(aceptaTerminos = null)) }
    }

    fun onTogglePasswordVisibility() {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun onToggleConfirmPasswordVisibility() {
        _uiState.update { it.copy(confirmPasswordVisible = !it.confirmPasswordVisible) }
    }

    /**
     * Valida el estado actual del formulario y actualiza el estado de los errores.
     * @return `true` si el formulario es válido, `false` si contiene errores.
     */
    fun validarFormulario(): Boolean {
        val state = _uiState.value
        val newErrors = RegisterErrorState(
            nombre = if (state.nombre.isBlank()) "El nombre es obligatorio" else null,
            apellido = if (state.apellido.isBlank()) "El apellido es obligatorio" else null,
            run = if (!isValidRun(state.run)) "El RUN no es válido" else null,
            email = if (!isValidEmail(state.email)) "El correo no es válido" else null,
            password = if (state.password.length < 4) "La contraseña debe tener al menos 4 caracteres" else null,
            confirmPassword = if (state.password != state.confirmPassword) "Las contraseñas no coinciden" else null,
            aceptaTerminos = if (!state.aceptaTerminos) "Debes aceptar los términos y condiciones" else null
        )

        _uiState.update { it.copy(errors = newErrors) }

        // El formulario es válido si no hay ningún mensaje de error en el objeto de errores.
        return newErrors.nombre == null && newErrors.apellido == null && newErrors.run == null &&
                newErrors.email == null && newErrors.password == null &&
                newErrors.confirmPassword == null && newErrors.aceptaTerminos == null
    }

    // --- Funciones de validación privadas ---

    private fun isValidEmail(email: String): Boolean {
        // Validación simple de formato de email (puedes hacerla más compleja)
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidRun(run: String): Boolean {
        // Validación simple de formato de RUN
        val regex = """^\d{7,8}-[\d|kK]$""".toRegex()
        return regex.matches(run)
    }
}
