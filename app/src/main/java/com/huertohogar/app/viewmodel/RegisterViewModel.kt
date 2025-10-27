package com.huertohogar.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.huertohogar.app.data.local.datastorage.SessionManager
import com.huertohogar.app.model.RegisterErrorState
import com.huertohogar.app.model.RegisterUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de Registro (RegisterScreen).
 * Contiene toda la lógica de negocio y el estado del formulario de registro.
 *
 * Hereda de 'AndroidViewModel' para tener acceso al 'application' context,
 * necesario para nuestro SessionManager.
 */
class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    // Creamos la instancia del SessionManager para poder guardar datos
    private val sessionManager = SessionManager(application)

    // _uiState es un flujo de datos mutable que contiene el estado actual del formulario.
    // Es privado para que solo el ViewModel pueda modificarlo.
    private val _uiState = MutableStateFlow(RegisterUiState())

    // uiState es la versión pública y de solo lectura de _uiState.
    // La interfaz de usuario (la pantalla) observa este flujo para reaccionar a los cambios.
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    // --- Funciones de actualización de estado (con reseteo de isLoading) ---

    fun onNombreChange(nombre: String) {
        _uiState.update { currentState ->
            currentState.copy(
                nombre = nombre,
                errors = currentState.errors.copy(nombre = null),
                isLoading = false
            )
        }
    }

    fun onApellidoChange(apellido: String) {
        _uiState.update { it.copy(apellido = apellido, errors = it.errors.copy(apellido = null), isLoading = false) }
    }

    fun onRunChange(run: String) {
        _uiState.update { it.copy(run = run, errors = it.errors.copy(run = null), isLoading = false) }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, errors = it.errors.copy(email = null), isLoading = false) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, errors = it.errors.copy(password = null), isLoading = false) }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword, errors = it.errors.copy(confirmPassword = null), isLoading = false) }
    }

    fun onAceptaTerminosChange(acepta: Boolean) {
        _uiState.update { it.copy(aceptaTerminos = acepta, errors = it.errors.copy(aceptaTerminos = null), isLoading = false) }
    }

    // --- Fin funciones de actualización ---

    fun onTogglePasswordVisibility() {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun onToggleConfirmPasswordVisibility() {
        _uiState.update { it.copy(confirmPasswordVisible = !it.confirmPasswordVisible) }
    }

    /**
     * Función principal que se llama desde la UI al pulsar "Crear Cuenta".
     * Gestiona el estado de carga y la navegación.
     * @param onRegisterSuccess Lambda que la UI pasa para ser llamada
     * cuando el registro sea exitoso.
     */
    fun onRegisterClicked(onRegisterSuccess: () -> Unit) {
        // 1. Ponemos el estado en "cargando"
        _uiState.update { it.copy(isLoading = true) }

        // 2. Validamos
        if (validarFormulario()) {
            // 3. Si es válido, lanzamos corrutina
            viewModelScope.launch {
                // 4. Guardamos email
                sessionManager.saveUserEmail(_uiState.value.email)

                // 5. Quitamos carga
                _uiState.update { it.copy(isLoading = false) }

                // 6. ¡Llamamos al callback para navegar!
                onRegisterSuccess()
            }
        } else {
            // 3b. Si NO es válido, quitamos la carga
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    /**
     * Valida el estado actual del formulario y actualiza el estado de los errores.
     * AHORA ES PRIVADA.
     * @return `true` si el formulario es válido, `false` si contiene errores.
     */
    private fun validarFormulario(): Boolean {
        // Obtiene el estado actual para no tener que repetirlo.
        val state = _uiState.value
        // Crea un nuevo objeto de errores basado en las validaciones.
        val newErrors = RegisterErrorState(
            nombre = if (state.nombre.isBlank()) "El nombre es obligatorio" else null,
            apellido = if (state.apellido.isBlank()) "El apellido es obligatorio" else null,
            run = if (!isValidRun(state.run)) "El RUN no es válido" else null,
            email = if (!isValidEmail(state.email)) "El correo no es válido" else null,
            password = if (state.password.length < 4) "La contraseña debe tener al menos 4 caracteres" else null,
            confirmPassword = if (state.password != state.confirmPassword) "Las contraseñas no coinciden" else null,
            aceptaTerminos = if (!state.aceptaTerminos) "Debes aceptar los términos y condiciones" else null
        )
        // Actualiza el estado de la UI con los nuevos errores que se encontraron.
        _uiState.update { it.copy(errors = newErrors) }

        // Devuelve 'true' solo si TODOS los campos de error son nulos (o sea, no hay errores).
        val isValid = newErrors.nombre == null && newErrors.apellido == null && newErrors.run == null &&
                newErrors.email == null && newErrors.password == null &&
                newErrors.confirmPassword == null && newErrors.aceptaTerminos == null

        return isValid
    }

    /**
     * Comprueba si un email tiene un formato válido.
     * @param email El correo a validar.
     * @return `true` si es un email válido.
     */
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * Comprueba si un RUN chileno tiene el formato correcto (ej: 12345678-9).
     * @param run El RUN a validar.
     * @return `true` si el formato es válido.
     */
    private fun isValidRun(run: String): Boolean {
        val regex = """^\d{7,8}-[\d|kK]$""".toRegex()
        return regex.matches(run)
    }
}

