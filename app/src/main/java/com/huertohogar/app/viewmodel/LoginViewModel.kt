package com.huertohogar.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.huertohogar.app.data.local.datastorage.SessionManager
import com.huertohogar.app.model.LoginErrorState
import com.huertohogar.app.model.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de Login (LoginScreen).
 * Contiene la lógica de negocio y el estado del formulario de inicio de sesión.
 *
 * AHORA hereda de 'AndroidViewModel' en lugar de 'ViewModel'.
 * Esto nos da acceso al 'application' context, que necesitamos para
 * inicializar nuestro SessionManager.
 *
 * @param application El contexto de la aplicación.
 */
class LoginViewModel(application: Application) : AndroidViewModel(application) {

    // --- ¡NUEVO! ---
    // 1. Creamos una instancia privada de nuestro SessionManager.
    // Le pasamos 'application' como contexto, que lo obtenemos del constructor.
    private val sessionManager = SessionManager(application)

    // _uiState es un flujo de datos que contiene el estado actual del formulario (email, contraseña, errores, etc.).
    // Es "mutable" porque puede cambiar, y "privado" para que solo el ViewModel lo modifique.
    private val _uiState = MutableStateFlow(LoginUiState())

    // uiState es la versión pública y de solo lectura de _uiState.
    // La pantalla (la UI) "escucha" los cambios en este flujo para repintarse cuando algo cambia.
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // --- Funciones para manejar eventos de la UI ---

    /**
     * Se llama cuando el usuario escribe en el campo de email.
     * @param email El texto actual en el campo de email.
     */
    fun onEmailChange(email: String) {
        // "update" es una forma segura de cambiar el estado.
        _uiState.update { currentState ->
            // Creamos una copia del estado actual, cambiando solo el email.
            currentState.copy(
                email = email,
                // Al escribir, limpiamos el error del email para que el mensaje desaparezca.
                errors = currentState.errors.copy(email = null),
                // Reseteamos el estado de carga si el usuario vuelve a escribir
                isLoading = false
            )
        }
    }

    /**
     * Se llama cuando el usuario escribe en el campo de contraseña.
     * @param password El texto actual en el campo de contraseña.
     */
    fun onPasswordChange(password: String) {
        _uiState.update { currentState ->
            currentState.copy(
                password = password,
                // También limpiamos el error de la contraseña al modificarla.
                errors = currentState.errors.copy(password = null),
                // Reseteamos el estado de carga
                isLoading = false
            )
        }
    }

    /**
     * Cambia el estado de visibilidad de la contraseña (de puntitos a texto y viceversa).
     */
    fun onTogglePasswordVisibility() {
        // Actualiza el estado, invirtiendo el valor booleano de passwordVisible.
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    /**
     * Función principal que se llama desde la UI al pulsar "Ingresar".
     * Gestiona el estado de carga y la navegación.
     * @param onLoginSuccess Es una función "lambda" que la UI nos pasa.
     * La llamaremos desde aquí (background) para que la UI sepa
     * cuándo debe navegar.
     */
    fun onLoginClicked(onLoginSuccess: () -> Unit) {
        // 1. Ponemos el estado en "cargando" para mostrar el loader
        _uiState.update { it.copy(isLoading = true) }

        // 2. Validamos el formulario (usando la función privada)
        if (validarFormulario()) {
            // 3. Si es válido, lanzamos la corrutina para guardar en DataStore
            viewModelScope.launch {
                // 4. Guardamos el email
                sessionManager.saveUserEmail(_uiState.value.email)

                // 5. Quitamos el estado de carga
                _uiState.update { it.copy(isLoading = false) }

                // 6. ¡Llamamos al "callback" para que la UI navegue!
                onLoginSuccess()
            }
        } else {
            // 3b. Si NO es válido, simplemente quitamos el estado de carga
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    /**
     * Valida el formulario de login.
     * Comprueba si el email es válido y si la contraseña no está vacía.
     * AHORA ES PRIVADA. La lógica de UI la maneja 'onLoginClicked'.
     *
     * @return `true` si es válido, `false` si contiene errores.
     */
    private fun validarFormulario(): Boolean {
        // Obtenemos el estado más reciente.
        val state = _uiState.value
        // Creamos un nuevo objeto de errores basado en las validaciones.
        val newErrors = LoginErrorState(
            email = if (!isValidEmail(state.email)) "El correo no es válido" else null,
            password = if (state.password.isBlank()) "La contraseña es obligatoria" else null
        )
        // Actualizamos el estado de la UI con los nuevos errores.
        _uiState.update { it.copy(errors = newErrors) }

        // El formulario es válido solo si no se encontró ningún error.
        val isValid = newErrors.email == null && newErrors.password == null

        //   TODO: En un proyecto real, aquí también llamaríamos al
        //  repositorio/API para verificar que el email Y la contraseña son correctos
        //  en el servidor. Por ahora, solo guardamos el email si el formato es válido.

        return isValid
    }

    /**
     * Función privada que comprueba si un email tiene un formato válido.
     * @param email El correo a validar.
     * @return `true` si el email no está vacío y cumple con el patrón de un correo.
     */
    private fun isValidEmail(email: String): Boolean {
        // Usamos una utilidad de Android que ya sabe cómo validar formatos de email.
        return email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
