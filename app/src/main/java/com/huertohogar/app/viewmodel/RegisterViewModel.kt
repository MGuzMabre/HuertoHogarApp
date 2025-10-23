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

    // _uiState es un flujo de datos mutable que contiene el estado actual del formulario.
    // Es privado para que solo el ViewModel pueda modificarlo.
    private val _uiState = MutableStateFlow(RegisterUiState())

    // uiState es la versión pública y de solo lectura de _uiState.
    // La interfaz de usuario (la pantalla) observa este flujo para reaccionar a los cambios.
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()


    /**
     * Esta función se llama cada vez que el usuario escribe en el campo de nombre.
     * Actualiza el valor del nombre en el estado y borra cualquier error asociado.
     * @param nombre El nuevo texto que el usuario ha introducido.
     */
    fun onNombreChange(nombre: String) {
        // "update" actualiza el estado de forma segura.
        _uiState.update { currentState ->
            currentState.copy(
                nombre = nombre,
                errors = currentState.errors.copy(nombre = null) // Limpia el error del nombre.
            )
        }
    }

    /**
     * Se activa cuando cambia el campo de apellido.
     * @param apellido El nuevo apellido introducido.
     */
    fun onApellidoChange(apellido: String) {
        _uiState.update { it.copy(apellido = apellido, errors = it.errors.copy(apellido = null)) }
    }

    /**
     * Se activa cuando cambia el campo de RUN.
     * @param run El nuevo RUN introducido.
     */
    fun onRunChange(run: String) {
        _uiState.update { it.copy(run = run, errors = it.errors.copy(run = null)) }
    }

    /**
     * Se activa cuando cambia el campo de email.
     * @param email El nuevo email introducido.
     */
    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, errors = it.errors.copy(email = null)) }
    }

    /**
     * Se activa cuando cambia el campo de contraseña.
     * @param password La nueva contraseña introducida.
     */
    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, errors = it.errors.copy(password = null)) }
    }

    /**
     * Se activa cuando cambia el campo de confirmar contraseña.
     * @param confirmPassword La nueva confirmación de contraseña.
     */
    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword, errors = it.errors.copy(confirmPassword = null)) }
    }

    /**
     * Se activa cuando el usuario marca o desmarca la casilla de aceptar términos.
     * @param acepta `true` si está marcada, `false` si no.
     */
    fun onAceptaTerminosChange(acepta: Boolean) {
        _uiState.update { it.copy(aceptaTerminos = acepta, errors = it.errors.copy(aceptaTerminos = null)) }
    }

    /**
     * Cambia la visibilidad de la contraseña (para mostrar/ocultar los caracteres).
     */
    fun onTogglePasswordVisibility() {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    /**
     * Cambia la visibilidad de la confirmación de contraseña.
     */
    fun onToggleConfirmPasswordVisibility() {
        _uiState.update { it.copy(confirmPasswordVisible = !it.confirmPasswordVisible) }
    }

    /**
     * Valida el estado actual del formulario y actualiza el estado de los errores.
     * @return `true` si el formulario es válido, `false` si contiene errores.
     */
    fun validarFormulario(): Boolean {
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
        return newErrors.nombre == null && newErrors.apellido == null && newErrors.run == null &&
                newErrors.email == null && newErrors.password == null &&
                newErrors.confirmPassword == null && newErrors.aceptaTerminos == null
        // TODO: La validación del RUN y la contraseña podrían ser más complejas y robustas.
        // Por ejemplo, verificar el dígito verificador del RUN o requerir caracteres especiales en la contraseña.
    }


    /**
     * Comprueba si un email tiene un formato válido.
     * @param email El correo a validar.
     * @return `true` si es un email válido.
     */
    private fun isValidEmail(email: String): Boolean {
        // Usa un patrón estándar de Android para validar emails. ¡Muy práctico!
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * Comprueba si un RUN chileno tiene el formato correcto (ej: 12345678-9).
     * @param run El RUN a validar.
     * @return `true` si el formato es válido.
     */
    private fun isValidRun(run: String): Boolean {
        // Una expresión regular para asegurar que el RUN tenga el formato correcto.
        val regex = """^\d{7,8}-[\d|kK]$""".toRegex()
        return regex.matches(run)
        // TODO: Esta validación solo comprueba el formato, no si el dígito verificador es correcto.
        // Se podría implementar el algoritmo del Módulo 11 para una validación completa.
    }
}
