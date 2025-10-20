package com.huertohogar.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.huertohogar.app.navigation.AppScreens
import com.huertohogar.app.ui.theme.HuertoHogarAppTheme
import com.huertohogar.app.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel()
) {
    val uiState by loginViewModel.uiState.collectAsState()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Iniciar Sesión", style = MaterialTheme.typography.headlineMedium)
            Text("Ingresa a tu cuenta para continuar", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(32.dp))

            // Campo Email
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { loginViewModel.onEmailChange(it) },
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.errors.email != null,
                supportingText = { if (uiState.errors.email != null) Text(uiState.errors.email!!) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Campo Contraseña
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { loginViewModel.onPasswordChange(it) },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.errors.password != null,
                supportingText = { if (uiState.errors.password != null) Text(uiState.errors.password!!) },
                visualTransformation = if (uiState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { loginViewModel.onTogglePasswordVisibility() }) {
                        Icon(
                            imageVector = if (uiState.passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Botón Ingresar
            Button(
                onClick = {
                    if (loginViewModel.validarFormulario()) {
                        // Lógica de login exitoso (por ahora, navegamos al Home)
                        navController.navigate(AppScreens.HomeScreen.route) {
                            // Limpia la pila de navegación para que el usuario no pueda volver al login
                            popUpTo(AppScreens.LoginScreen.route) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ingresar")
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Botón para ir a Registro
            TextButton(onClick = { navController.navigate(AppScreens.RegisterScreen.route) }) {
                Text("¿No tienes una cuenta? Crear Cuenta")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    HuertoHogarAppTheme {
        LoginScreen(navController = NavController(LocalContext.current))
    }
}

