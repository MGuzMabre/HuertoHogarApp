plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // Quitamos la referencia explícita al plugin de compose aquí si no se usa en otro lado
    // alias(libs.plugins.kotlin.compose) //<- Comentado o eliminado si no se necesita explícitamente
    // Asegúrate que el plugin de compose se aplica de otra forma si es necesario,
    // a menudo viene implícito con el kotlin-android plugin o configuraciones específicas.
    // O si usas 'org.jetbrains.kotlin.plugin.compose', añádelo así:
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.21" // Asegúrate que la versión coincida con tu Kotlin
}

android {
    namespace = "com.huertohogar.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.huertohogar.app"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true // Habilitamos Compose
    }
    // Quitamos TODO el bloque composeOptions.
    // La versión del compilador la gestiona el BOM y AGP.
    /*
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    */
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom)) // El BOM ya está aquí
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.coil.compose)
    implementation(libs.androidx.datastore.preferences)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}