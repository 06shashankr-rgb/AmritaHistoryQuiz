plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.amritahistoryquiz"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.amritahistoryquiz"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.7.0")
}
