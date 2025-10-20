package com.huertohogar.app.model

/**
 * Representa el estado completo del formulario de registro en un momento dado.
 * Contiene el valor de cada campo y los posibles errores de validación.
 *
 * @param nombre El nombre ingresado por el usuario.
 * @param apellido El apellido ingresado por el usuario.
 * @param run El RUN ingresado por el usuario.
 * @param email El correo electrónico ingresado.
 * @param password La contraseña ingresada.
 * @param confirmPassword La confirmación de la contraseña.
 * @param aceptaTerminos Si el usuario aceptó los términos y condiciones.
 * @param passwordVisible Controla si la contraseña es visible o no.
 * @param errors Un objeto que contiene los mensajes de error para cada campo.
 */
data class RegisterUiState(
    val nombre: String = "",
    val apellido: String = "",
    val run: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val aceptaTerminos: Boolean = false,
    val passwordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean = false,
    val errors: RegisterErrorState = RegisterErrorState()
)

/**
 * Contiene los posibles mensajes de error para cada campo validable del formulario de registro.
 * Un valor 'null' significa que no hay error en ese campo.
 */
data class RegisterErrorState(
    val nombre: String? = null,
    val apellido: String? = null,
    val run: String? = null,
    val email: String? = null,
    val password: String? = null,
    val confirmPassword: String? = null,
    val aceptaTerminos: String? = null
)