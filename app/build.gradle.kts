plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.opsc7213_goalignite"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.opsc7213_goalignite"
        minSdk = 34
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
    packagingOptions {
        resources {
            excludes += "/META-INF/DEPENDENCIES"
            excludes += "/META-INF/LICENSE"
            excludes += "/META-INF/LICENSE.txt"
            excludes += "/META-INF/license.txt"
            excludes += "/META-INF/NOTICE"
            excludes += "/META-INF/NOTICE.txt"
            excludes += "/META-INF/notice.txt"
        }
    }
}
    dependencies {
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.appcompat)
        implementation(libs.material)
        implementation(libs.androidx.activity)
        implementation(libs.androidx.constraintlayout)
        implementation(libs.circleimageview)
        implementation(libs.sdp)
        implementation(libs.firebase.database)
        implementation(libs.firebase.auth)
        implementation(libs.firebase.firestore.ktx)
        implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
        implementation("com.google.android.gms:play-services-auth:21.2.0")
        implementation(libs.androidx.recyclerview)
        implementation(libs.firebase.firestore)
        implementation(libs.places)
        implementation(libs.firebase.storage.ktx)

        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
        implementation("androidx.recyclerview:recyclerview:1.3.2") // Keep this version if it's needed
        implementation("androidx.biometric:biometric-ktx:1.4.0-alpha02") // Consider stable versions
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")
        implementation("com.facebook.android:facebook-login:[latest-version]") // Replace with a stable version
        implementation("com.facebook.android:facebook-android-sdk:16.0.1")

        // Retrofit and OkHttp
        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")
        implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

        // Glide
        implementation("com.github.bumptech.glide:glide:4.14.2")
        annotationProcessor("com.github.bumptech.glide:compiler:4.14.2")

        // Firebase Auth
        implementation("com.google.firebase:firebase-auth") // This may already be covered by BOM

        // Google Drive and API Client
        implementation("com.google.android.gms:play-services-drive:17.0.0")
        implementation ("com.google.api-client:google-api-client:1.32.2")
        implementation("com.google.api-client:google-api-client-android:1.32.2")
        implementation("com.google.api-client:google-api-client-gson:1.32.2")
        implementation("com.google.apis:google-api-services-drive:v3-rev136-1.25.0")
        implementation ("com.google.http-client:google-http-client-android:1.40.1")
        implementation("com.google.http-client:google-http-client-jackson2:1.41.5")

        // Guava
        implementation("com.google.guava:guava:32.1.2-jre")
        implementation("com.google.android.gms:play-services-fido:21.1.0")

        // Coroutine dependencies
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

        implementation ("com.squareup.picasso:picasso:2.5.2")
        implementation ("androidx.media:media:1.4.0")
    }
