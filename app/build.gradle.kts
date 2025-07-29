plugins {
    alias(libs.plugins.ksp) // ✅ Dùng alias
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    kotlin("kapt")
    id("com.google.gms.google-services")
}

hilt {
    enableAggregatingTask = false
}

configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains.kotlin") {
            useVersion("1.9.0")
        }
    }
}

android {
    namespace = "com.example.weatherapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.weatherapp"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    //hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.javapoet)
    //room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    // Nếu dùng annotation
    implementation(libs.androidx.room.ktx)
    //Navigation Component (Compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    //Coroutines
    //noinspection GradleDependency,GradleDependency
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    //Retrofit + Gson
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    //Coil (load ảnh thời tiết)
    implementation(libs.coil.compose)
    // ViewModel + Lifecycle (Jetpack Compose)
    implementation(libs.androidx.lifecycle.runtime.ktx.v270)     // ✅ đúng version 2.7.0
    implementation(libs.androidx.lifecycle.viewmodel.compose)    // ✅ nếu đã fix version về 2.7.0
    //okhttp
    //noinspection UseTomlInstead
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.11")
    //firebase
    implementation(libs.firebase.auth.ktx)
    //coil
    implementation("io.coil-kt:coil-compose:2.4.0")

}