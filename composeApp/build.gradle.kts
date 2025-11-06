import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.apollo)
    alias(libs.plugins.sqldelight)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            // Koin Android
            implementation(libs.koin.android)

            // SQLDelight Android Driver
            implementation(libs.sqldelight.android)

            // Coroutines Android
            implementation(libs.kotlinx.coroutines.android)
        }

        iosMain.dependencies {
            // SQLDelight Native Driver
            implementation(libs.sqldelight.native)
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // Apollo GraphQL
            implementation(libs.apollo.runtime)

            // Koin Core & Compose
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            // SQLDelight
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines)

            // Coroutines
            implementation(libs.kotlinx.coroutines.core)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

android {
    namespace = "com.honcharenko.reposbrowser"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.honcharenko.reposbrowser"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

// Configure Apollo
apollo {
    service("github") {
        // Package name for generated code
        packageName.set("com.honcharenko.reposbrowser.data.graphql")

        // Schema location - GitHub GraphQL schema
        schemaFiles.from(file("src/commonMain/graphql/schema.graphqls"))

        // Optional: Generate Kotlin models as data classes
        generateKotlinModels.set(true)

        // Optional: Use semantic nullability (experimental in v4)
        // generateOptionalOperationVariables.set(false)
    }
}

// Configure SQLDelight
sqldelight {
    databases {
        create("ReposBrowserDatabase") {
            packageName.set("com.honcharenko.reposbrowser.data.local")
            srcDirs.setFrom("src/commonMain/sqldelight")
        }
    }
}
