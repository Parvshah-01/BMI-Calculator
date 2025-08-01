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
    implementation(libs.androidx.foundation)
    implementation(libs.ui)
    implementation("androidx.compose.material3:material3:1.1.0")
    implementation("androidx.compose.material:material:1.6.5") // ← Added
    implementation("androidx.compose.animation:animation:1.7.6")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.6")
    implementation("androidx.compose.runtime:runtime-livedata:1.7.6")

    // Gson for JSON
    implementation("com.google.code.gson:gson:2.10.1") // ← Added

    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx.v260)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Navigation Compose
    implementation(libs.androidx.navigation.compose)

    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose.v1100)

    // Compose BOM
    implementation(platform(libs.androidx.compose.bom.v20250100))

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.junit.v115)
    androidTestImplementation(libs.androidx.espresso.espresso.core)
    androidTestImplementation(libs.androidx.compose.compose.bom)

    // Debugging
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}


