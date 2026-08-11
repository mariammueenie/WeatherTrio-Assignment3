plugins {
    alias(libs.plugins.android.application)

    // Connects this app module to Firebase.
    id("com.google.gms.google-services")
}

android {
    namespace = "com.mariammueen.weathertrio"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mariammueen.weathertrio"
        minSdk = 33
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

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

    // View Binding was required by the assignment and taught in class.
    // It creates a binding class for every XML layout so the Java code does
    // not need findViewById().
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // RecyclerView displays the required scrolling list of weather locations.
    // Version 1.3.2 is used because this project compiles against Android API 34.
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // ViewModel stores and manages UI-related data.
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.8.7")

    // LiveData allows the Fragment to observe weather, loading, and error states.
    implementation("androidx.lifecycle:lifecycle-livedata:2.8.7")

    // OkHttp performs the OpenMateo network request asynchronously.
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Keeps all Firebase libraries on compatible versions.
    // Assignment 3 / class material specifies BoM 33.7.0.
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))

    // Firebase Authentication will provide registration,
    // login, current-user sessions and sign out.
    implementation("com.google.firebase:firebase-auth")

    // Cloud Firestore stores each user's saved weather locations.
    implementation("com.google.firebase:firebase-firestore")
}
