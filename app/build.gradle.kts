plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.jetbrainsDokka)
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.androidx.recyclerview)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.runtime.livedata)

    implementation(libs.zxing)

    implementation(libs.ktor.core)
    implementation(libs.ktor.server)
    implementation(libs.koin.ktor)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //noinspection UseTomlInstead
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}