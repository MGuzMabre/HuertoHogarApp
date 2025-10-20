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
import com.huertohogar.app.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    registerViewModel: RegisterViewModel = viewModel()
) {
    val uiState by registerViewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()), // Permite hacer scroll si el contenido no cabe
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Crear Cuenta", style = MaterialTheme.typography.headlineMedium)
            Text("Regístrate para disfrutar de nuestros servicios", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(32.dp))

            // Campo Nombre
            OutlinedTextField(
                value = uiState.nombre,
                onValueChange = { registerViewModel.onNombreChange(it) },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.errors.nombre != null,
                supportingText = { if (uiState.errors.nombre != null) Text(uiState.errors.nombre!!) }
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Campo Apellido
            OutlinedTextField(
                value = uiState.apellido,
                onValueChange = { registerViewModel.onApellidoChange(it) },
                label = { Text("Apellido") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.errors.apellido != null,
                supportingText = { if (uiState.errors.apellido != null) Text(uiState.errors.apellido!!) }
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Campo RUN
            OutlinedTextField(
                value = uiState.run,
                onValueChange = { registerViewModel.onRunChange(it) },
                label = { Text("RUN (sin puntos, con guión)") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.errors.run != null,
                supportingText = { if (uiState.errors.run != null) Text(uiState.errors.run!!) },
                placeholder = { Text("12345678-9") }
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Campo Email
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { registerViewModel.onEmailChange(it) },
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
                onValueChange = { registerViewModel.onPasswordChange(it) },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.errors.password != null,
                supportingText = { if (uiState.errors.password != null) Text(uiState.errors.password!!) },
                visualTransformation = if (uiState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { registerViewModel.onTogglePasswordVisibility() }) {
                        Icon(
                            imageVector = if (uiState.passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Campo Confirmar Contraseña
            OutlinedTextField(
                value = uiState.confirmPassword,
                onValueChange = { registerViewModel.onConfirmPasswordChange(it) },
                label = { Text("Confirmar Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.errors.confirmPassword != null,
                supportingText = { if (uiState.errors.confirmPassword != null) Text(uiState.errors.confirmPassword!!) },
                visualTransformation = if (uiState.confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { registerViewModel.onToggleConfirmPasswordVisibility() }) {
                        Icon(
                            imageVector = if (uiState.confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Toggle confirm password visibility"
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Checkbox Términos y Condiciones
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = uiState.aceptaTerminos,
                    onCheckedChange = { registerViewModel.onAceptaTerminosChange(it) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Acepto los términos y condiciones", style = MaterialTheme.typography.bodyMedium)
            }
            if (uiState.errors.aceptaTerminos != null) {
                Text(
                    text = uiState.errors.aceptaTerminos!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Crear Cuenta
            Button(
                onClick = {
                    if (registerViewModel.validarFormulario()) {
                        // Lógica de registro exitoso (por ahora, navegamos al Home)
                        navController.navigate(AppScreens.HomeScreen.route) {
                            popUpTo(AppScreens.LoginScreen.route) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Crear Cuenta")
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { navController.navigate(AppScreens.LoginScreen.route) }) {
                Text("¿Ya tienes una cuenta? Inicia Sesión")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    HuertoHogarAppTheme {
        // Para la vista previa, creamos un NavController de ejemplo que no hace nada.
        RegisterScreen(navController = NavController(LocalContext.current))
    }
}
