plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.bmicalculator"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.bmicalculator"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        compose = true
    }
}


dependencies {
    // Compose
    implementation(libs.androidx.foundation) // For swipeable functionality
    implementation(libs.ui) // Basic UI components
    implementation("androidx.compose.material3:material3:1.1.0") // For Material 3 components (including TextField)
    implementation("androidx.compose.animation:animation:1.7.6") // For animation functionality
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.6") // For UI preview
    implementation("androidx.compose.runtime:runtime-livedata:1.7.6") // For LiveData support

    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx.v260) // Lifecycle runtime
    implementation(libs.androidx.lifecycle.livedata.ktx) // LiveData support
    implementation(libs.androidx.lifecycle.viewmodel.compose) // ViewModel Compose support

    // Navigation Compose
    implementation(libs.androidx.navigation.compose) // Navigation Compose support

    // Core libraries
    implementation(libs.androidx.core.ktx) // Core KTX
    implementation(libs.androidx.activity.compose.v1100) // Activity Compose support

    // Optional Compose BOM (Bill of Materials)
    implementation(platform(libs.androidx.compose.bom.v20250100)) // BOM for Compose

    // Testing dependencies
    testImplementation(libs.junit) // Unit testing
    androidTestImplementation(libs.junit.v115) // JUnit for Android testing
    androidTestImplementation(libs.androidx.espresso.espresso.core) // Espresso for UI testing
    androidTestImplementation(libs.androidx.compose.compose.bom) // Compose BOM for testing

    // Debugging tools
    debugImplementation(libs.ui.tooling) // UI tooling for debugging
    debugImplementation(libs.ui.test.manifest) // UI test manifest for debugging
}

