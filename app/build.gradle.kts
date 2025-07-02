// Di bagian atas file, setelah plugins
import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.jukang"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.jukang"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val localProperties = Properties()
        val localProperyiesFiles = rootProject.file("local.properties")

        if(localProperyiesFiles.exists()){
            localProperties.load(FileInputStream(localProperyiesFiles))
        }

        buildConfigField("String", "GEMINI_API_KEY", "\"${localProperties.getProperty("GEMINI_API_KEY")}\"")
        buildConfigField("String", "CLIENT_KEY_MT", "\"${localProperties.getProperty("CLIENT_KEY_MT")}\"")
        buildConfigField("String", "SERVER_KEY_MT", "\"${localProperties.getProperty("SERVER_KEY_MT")}\"")

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
        viewBinding = true
        mlModelBinding = true
        buildConfig = true
    }


}

dependencies {
    implementation(libs.tensorflow.lite.metadata)
    // Ganti semua baris tensorflow-lite Anda dengan ini

    val tflite_version = "2.16.1"

    implementation ("org.tensorflow:tensorflow-lite:$tflite_version")
    implementation ("org.tensorflow:tensorflow-lite-support:0.4.4") // Versi support library tetap berbeda
//    implementation ("org.tensorflow:tensorflow-lite-gpu-delegate-plugin:$tflite_version")
    implementation ("org.tensorflow:tensorflow-lite-select-tf-ops:$tflite_version")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.gridlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

//    lifecycle
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

//    room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)

//    Glide
    implementation(libs.glide)

//    GIF
    implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.29")
    implementation("com.google.ai.client.generativeai:generativeai:0.5.0") // Gemini API
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0") // Untuk viewModelScope

// retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.retrofit.mock)
    implementation(libs.logging.interceptor)
    implementation(libs.mockwebserver)

    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.android.gms:play-services-auth:21.3.0")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation ("com.google.firebase:firebase-storage:21.0.1")


    implementation("com.github.razaghimahdi:Android-Loading-Dots:1.3.2")

    implementation ("com.github.ismaeldivita:chip-navigation-bar:1.4.0")
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    implementation("androidx.datastore:datastore:1.1.1")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.datastore:datastore-core:1.1.1")

    implementation ("com.google.android.gms:play-services-location:21.3.0")

    implementation("org.osmdroid:osmdroid-android:6.1.18")

    implementation("androidx.camera:camera-camera2:1.4.2")
    implementation("androidx.camera:camera-lifecycle:1.4.2")
    implementation("androidx.camera:camera-view:1.4.2")
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.camera:camera-extensions:1.4.2")
    implementation("androidx.camera:camera-core:1.4.2")

    implementation ("com.airbnb.android:lottie:6.6.7")

    implementation("com.midtrans:uikit:2.3.0-SANDBOX")


}