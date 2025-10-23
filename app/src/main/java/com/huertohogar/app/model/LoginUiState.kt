package com.huertohogar.app.model

/**
 *
 * @param email El correo electrónico ingresado por el usuario.
 * @param password La contraseña ingresada.
 * @param passwordVisible Controla si la contraseña es visible o no.
 * @param errors Un objeto que contiene los mensajes de error para cada campo.
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val errors: LoginErrorState = LoginErrorState()
)

/**
 * Contiene los posibles mensajes de error para cada campo del formulario de login.
 * Un valor 'null' significa que no hay error.
 */
data class LoginErrorState(
    val email: String? = null,
    val password: String? = null
)
