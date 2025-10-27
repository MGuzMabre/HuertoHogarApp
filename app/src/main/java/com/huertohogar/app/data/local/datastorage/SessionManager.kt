package com.huertohogar.app.data.local.datastorage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión de Context para crear una instancia única de DataStore
// con el nombre "user_session_prefs"
private val Context.dataStore by preferencesDataStore(name = "user_session_prefs")

/**
 * Gestiona la persistencia de datos de la sesión del usuario (ej. email)
 * usando DataStore Preferences.
 *
 * @param context El contexto de la aplicación, necesario para inicializar DataStore.
 */
class SessionManager(context: Context) {

    // Instancia de DataStore referenciada desde el contexto
    private val dataStore = context.dataStore

    // Creamos una clave (Key) para el email. Es como el "nombre" de la variable
    // que guardaremos en DataStore.
    companion object {
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
    }

    /**
     * Guarda el email del usuario en DataStore de forma asíncrona.
     * @param email El email a guardar.
     */
    suspend fun saveUserEmail(email: String) {
        // 'edit' es una función de suspensión (suspend fun) que nos permite
        // modificar las preferencias de forma segura.
        dataStore.edit { preferences ->
            preferences[USER_EMAIL_KEY] = email
        }
    }

    /**
     * Obtiene el email del usuario como un Flow.
     * Un Flow permite "observar" el dato, de modo que si cambia,
     * la UI se actualiza automáticamente.
     * @return Un Flow<String?> que emite el email guardado, o null si no hay ninguno.
     */
    fun getUserEmail(): Flow<String?> {
        // 'dataStore.data' es el Flow que emite las preferencias
        // cada vez que cambian.
        return dataStore.data
            .map { preferences ->
                // Leemos el valor asociado a nuestra clave USER_EMAIL_KEY
                preferences[USER_EMAIL_KEY]
            }
    }

    /**
     * Borra el email del usuario de DataStore.
     * Esto se usará para el "Cerrar Sesión".
     */
    suspend fun clearUserEmail() {
        dataStore.edit { preferences ->
            // Simplemente removemos la clave de las preferencias
            preferences.remove(USER_EMAIL_KEY)
        }
    }
}