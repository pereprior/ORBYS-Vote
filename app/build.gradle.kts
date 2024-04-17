plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.ksp)
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.dokka")
}

android {
    namespace = "com.orbys.quizz"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.orbys.quizz"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            // optimizacion y ofuscacion de clases y funciones
            isMinifyEnabled = false
            // optimizacion de los recursos
            isShrinkResources = false

            // @Keep para ignorar clases
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    packagingOptions {
        exclude("META-INF/INDEX.LIST")
        exclude("META-INF/io.netty.versions.properties")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    // ViewBinding
    buildFeatures {
        viewBinding = true
    }

}

dependencies {

    // Kotlin Basics
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // QrCode
    implementation(libs.zxing)

    // Ktor server
    implementation(libs.ktor.core)
    implementation(libs.ktor.server)
    implementation(libs.ktor.server.netty)

    // Inyeccion de dependencias
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Workbook
    //implementation(libs.poi.ooxml)

}