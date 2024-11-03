plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.arquitectura.apirest"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.arquitectura.apirest"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    val roomVersion = "2.6.1"
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation(libs.room.common)
    androidTestImplementation ("androidx.room:room-testing:$rootProject.roomVersion")
    implementation("androidx.room:room-runtime:2.5.0")
    annotationProcessor("androidx.room:room-compiler:2.5.0")
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

}