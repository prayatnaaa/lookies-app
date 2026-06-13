import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")

}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.fromTarget("17")
    }
}

android {
    namespace = "com.prayatna.lookiesapp"
    compileSdk = 36

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    defaultConfig {
        applicationId = "com.prayatna.lookiesapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())
        val baseUrl = properties.getProperty("BASE_URL") ?: ""
        val apiKey = properties.getProperty("API_KEY") ?: ""
        val supabaseEdgeBaseUrl = properties.getProperty("SUPABASE_EDGE_BASE_URL") ?: ""
        val xenditSecretKey = properties.getProperty("XENDIT_SECRET_KEY") ?: ""

        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
        buildConfigField("String", "API_KEY", "\"$apiKey\"")
        buildConfigField("String", "SUPABASE_EDGE_BASE_URL", "\"$supabaseEdgeBaseUrl\"")
        buildConfigField("String", "XENDIT_SECRET_KEY", "\"$xenditSecretKey\"")
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
}

dependencies {
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.paging)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.datastore.preferences)

    //Supabase Setup
    implementation(libs.ktor.client.cio)
    implementation(libs.gotrue.kt)
    implementation(libs.storage.kt)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.utils.jvm)
    implementation(libs.ktor.client.logging.jvm)
    implementation(libs.coil.compose)
    implementation(libs.postgrest.kt)
    implementation(libs.realtime.kt)

    //Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.runtime)
    implementation(libs.androidx.runtime.livedata)

    // Coil
    implementation(libs.coil3.coil.compose)
    implementation(libs.coil.network.okhttp)

    //JWT Decode
    implementation(libs.jwtdecode)

    //Desugar
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    //ZXING (Barcode generator)
    implementation(libs.core)

    //Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)

    //Worker
    implementation(libs.androidx.work.runtime.ktx)

    //Ktor
    implementation(libs.ktor.client.okhttp)

    // ML Kit Barcode Scanning
    implementation(libs.barcode.scanning)
    //camera
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)

    //viewModel lifeCycle
    implementation (libs.androidx.lifecycle.viewmodel.compose)
}