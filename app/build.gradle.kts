plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.testresultapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.testresultapp"
        minSdk = 35
        targetSdk = 35
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
}

// build.gradle.kts (app)
dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)

    // Room komponenty
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")

    // PDF generování
    implementation("com.itextpdf:itext7-core:8.0.2")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}