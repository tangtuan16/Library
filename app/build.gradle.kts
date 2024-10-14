plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.btl_libary"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.btl_libary"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Thêm cấu hình MultiDex
        multiDexEnabled = true
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

    buildFeatures {
        viewBinding = true
    }
}



dependencies {
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("com.google.android.material:material:1.3.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.play.services.maps)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)

    // Cập nhật thư viện core và core-ktx
    implementation("androidx.core:core:1.10.1")
    implementation("androidx.core:core-ktx:1.10.1")

    // Thêm thư viện multidex
    implementation("androidx.multidex:multidex:2.0.1")

    implementation("com.squareup.picasso:picasso:2.8")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("org.tensorflow:tensorflow-lite:2.5.0")
    implementation("com.google.code.gson:gson:2.9.1")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation ("androidx.work:work-runtime-ktx:2.7.1")
    implementation ("com.google.guava:guava:31.0.1-android")

    implementation ("com.sun.mail:android-mail:1.6.2")
    implementation ("com.sun.mail:android-activation:1.6.2")


    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

// Thêm cấu hình này để giải quyết xung đột về các phiên bản của thư viện
configurations.all {
    resolutionStrategy {
        force("org.jetbrains.kotlin:kotlin-stdlib:1.8.20")
        force("org.jetbrains.kotlin:kotlin-stdlib-common:1.8.20")
    }
}