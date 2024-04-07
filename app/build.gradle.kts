plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.jetbrainsDokka)
    alias(libs.plugins.ksp)
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.pprior.quizz"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.pprior.quizz"
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    // ViewBinding
    buildFeatures {
        viewBinding = true
    }

    // Documentacion
    subprojects {
        apply(plugin = "org.jetbrains.dokka")
    }

    packagingOptions {
        exclude("META-INF/INDEX.LIST")
        exclude("META-INF/io.netty.versions.properties")
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

    // RecyclerView
    implementation(libs.androidx.recyclerview)

    // QrCode
    implementation(libs.zxing)

    // Ktor server
    implementation(libs.ktor.core)
    implementation(libs.ktor.server)
    implementation(libs.ktor.server.netty)

    // Inyeccion de dependencias
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

}