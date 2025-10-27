package com.huertohogar.app.ui.screens

import android.Manifest // Importamos el permiso
import android.content.pm.PackageManager // Importamos el gestor de permisos
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource // Para el icono de perfil por defecto
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat // Importamos el chequeador de permisos
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.huertohogar.app.R // Necesario para R.drawable.ic_person_placeholder
import com.huertohogar.app.navigation.AppScreens
import com.huertohogar.app.ui.theme.HuertoHogarAppTheme
import com.huertohogar.app.viewmodel.ProfileViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = viewModel()
) {
    // Observamos el estado completo (que ahora incluye email, imagen y estado de carga)
    val uiState by profileViewModel.uiState.collectAsState()
    val context = LocalContext.current

    // --- Launchers para Cámara y Galería ---

    // Variable temporal para guardar la URI generada para la cámara
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    /**
     * Genera una URI de archivo temporal en el almacenamiento INTERNO de la app.
     * Esta es la forma segura de hacerlo para evitar crashes si el almacenamiento
     * externo no está disponible.
     */
    fun getTmpFileUri(): Uri {
        // Usamos 'context.filesDir' (almacenamiento interno)
        // que está garantizado que no será null.
        val storageDir: File = context.filesDir

        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())

        val tmpFile = File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir // Usamos el directorio interno
        ).apply {
            createNewFile()
            // Opcional: Borra el archivo cuando la JVM de la app se cierre
            // si no se ha movido o usado permanentemente.
            deleteOnExit()
        }

        // Usamos el FileProvider para crear una URI "content://" segura para la cámara.
        return FileProvider.getUriForFile(
            context, // 'context' ya no necesita Objects.requireNonNull
            // IMPORTANTE: Esta "autoridad" DEBE COINCIDIR
            // con la definida en 'AndroidManifest.xml'
            "com.huertohogar.app.provider",
            tmpFile
        )
    }

    // --- Definición de todos los Launchers ---

    // 1. Launcher para TOMAR FOTO (TakePicture)
    // Este launcher solo se llama DESPUÉS de tener el permiso
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        // El resultado es un booleano: true si la foto se guardó, false si se canceló.
        if (success) {
            // Leemos el valor en una variable local ANTES de usarlo
            val currentUri = tempCameraUri
            if (currentUri != null) {
                // Si fue exitoso y la URI no es nula, actualizamos el ViewModel
                profileViewModel.updateProfileImage(currentUri)
            } else {
                // Podríamos mostrar un error si la URI es nula inesperadamente
                Toast.makeText(context, "Error: No se pudo obtener la URI de la imagen capturada.", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Si canceló, podemos mostrar un mensaje (opcional)
            Toast.makeText(context, "Captura cancelada", Toast.LENGTH_SHORT).show()
        }
    }

    // 2. Launcher para SELECCIONAR IMAGEN (GetContent - Galería)
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        // Cuando el usuario selecciona una imagen (o cancela),
        // actualizamos el ViewModel con la URI resultante.
        profileViewModel.updateProfileImage(uri)
    }

    // 3. Launcher para PEDIR PERMISO (RequestPermission)
    // Este es el nuevo launcher que maneja la solicitud de permiso de CÁMARA
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // El usuario CONCEDIÓ el permiso.
            // Ahora sí, lanzamos la cámara.
            try {
                // Asignamos a una local 'val' y la usamos para ambas operaciones
                val newUri = getTmpFileUri()
                tempCameraUri = newUri // Actualizamos el estado
                cameraLauncher.launch(newUri) // Lanzamos con la variable local
            } catch (e: Exception) {
                Toast.makeText(context, "Error al preparar la cámara: ${e.message}", Toast.LENGTH_LONG).show()
            }
        } else {
            // El usuario DENEGÓ el permiso.
            Toast.makeText(context, "Permiso de cámara denegado.", Toast.LENGTH_SHORT).show()
        }
    }


    // --- Interfaz de Usuario ---
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        // Hacemos la columna "scrollable" por si el botón de logout
        // queda muy abajo en pantallas pequeñas
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // --- Imagen de Perfil ---
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape) // Recorta la imagen en forma circular
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { /* Podríamos añadir opción para ver imagen en grande */ },
                contentAlignment = Alignment.Center
            ) {
                // Si hay una URI de imagen en el estado, la muestra con AsyncImage
                if (uiState.profileImageUri != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(uiState.profileImageUri)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.fillMaxSize(), // Llena el círculo
                        contentScale = ContentScale.Crop // Recorta para llenar
                    )
                } else {
                    // Si no hay imagen, muestra un icono de persona por defecto
                    Icon(
                        painter = painterResource(id = R.drawable.ic_person_placeholder), // Usamos el placeholder
                        contentDescription = "Sin foto de perfil",
                        modifier = Modifier.size(80.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Email del Usuario (Leído desde DataStore) ---
            if (uiState.isLoading) {
                // Mostramos un indicador de carga mientras leemos el DataStore
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                // Cuando termina de cargar, mostramos el email o un texto por defecto
                Text(
                    text = uiState.userEmail ?: "Email no disponible",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }


            Spacer(modifier = Modifier.height(32.dp))

            // --- Botones para Cámara y Galería ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Botón Cámara
                Button(
                    onClick = {
                        // Lógica de click en botón CÁMARA
                        // 1. Comprobar si el permiso ya está concedido
                        when (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA // El permiso que queremos chequear
                        )) {
                            PackageManager.PERMISSION_GRANTED -> {
                                // 2. Si ya está concedido, lanzamos la cámara directamente.
                                try {
                                    // --- ¡ARREGLO 3! ---
                                    // Mismo patrón que en permissionLauncher
                                    val newUri = getTmpFileUri()
                                    tempCameraUri = newUri // Actualizamos estado
                                    cameraLauncher.launch(newUri) // Lanzamos con local val
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Error al abrir la cámara: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                            else -> {
                                // 3. Si no está concedido, lanzamos el launcher de PERMISO.
                                // Este launcher mostrará el pop-up al usuario.
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Tomar Foto")
                }

                // Botón Galería
                Button(
                    onClick = {
                        // Lanza el selector de galería, pidiendo solo imágenes
                        // Para la galería no se necesita permiso de runtime (GetContent)
                        galleryLauncher.launch("image/*")
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Galería")
                }
            }

            // Espacio para empujar el botón de logout al fondo
            Spacer(modifier = Modifier.weight(1f))

            // --- Botón de Cerrar Sesión ---
            OutlinedButton(
                onClick = {
                    // 1. Llama al ViewModel para limpiar los datos
                    profileViewModel.onLogout()

                    // 2. Navega al Login y limpia el historial
                    navController.navigate(AppScreens.LoginScreen.route) {
                        // Limpia TODA la pila de navegación hasta el inicio
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true // Borra también el destino inicial
                        }
                        // Nos aseguramos de que solo haya una instancia de Login
                        launchSingleTop = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error // Color rojo para el texto y el icono
                )
            ) {
                // Usamos un icono de "salir"
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Cerrar Sesión")
            }

            // Un pequeño espacio al final para que no quede pegado al borde
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// --- Vista Previa ---
@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    HuertoHogarAppTheme {
        ProfileScreen(navController = NavController(LocalContext.current))
    }
}

