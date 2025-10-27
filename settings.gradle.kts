pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven {
            url = uri("https://maven.pkg.github.com/ElMabre/HuertoHogarApp")

            // Solo intentar configurar credenciales SI existen en un archivo gradle.properties
            // Esto evita que el build falle para usuarios nuevos.
            if (extra.has("gpr.user") && extra.has("gpr.key")) {
                credentials {
                    username = extra["gpr.user"] as String
                    password = extra["gpr.key"] as String
                }
            }

        }
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    // Mantiene el modo estricto, pero ahora el repo de GitHub está permitido globalmente
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Repositorio GitHub Packages para dependencias privadas
        maven {
            url = uri("https://maven.pkg.github.com/ElMabre/HuertoHogarApp")
            
            // Se repite la misma lógica aquí para las dependencias del proyecto
            if (extra.has("gpr.user") && extra.has("gpr.key")) {
                credentials {
                    username = extra["gpr.user"] as String
                    password = extra["gpr.key"] as String
                }
            }
        }
    }
}

rootProject.name = "HuertoHogarApp"
include(":app")
