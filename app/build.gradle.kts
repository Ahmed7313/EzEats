plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-kapt")
}

android {
    namespace = "com.easyEats.ezeats"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.easyEats.ezeats"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            buildConfigField(
                "String", "APPLICATION_ID", "\"7ei5pxppEe8ErCMB6Ehm6HTIJRcdJJd7SRHUpN8n\""
            )
            buildConfigField("String", "CLIENT_KEY", "\"ko0v0w3Xl6jAZP5vhKxQj3HR7Ne8TcQJ7cHpJRIn\"")
            buildConfigField(
                "String",
                "URL",
                "\"https://pg-app-jtxlimrcv63uszqdkzh2a9d1r7pd0u.scalabl.cloud/1/\""
            )
            buildConfigField(
                "String",
                "BASE_URL",
                "\"https://pg-app-jtxlimrcv63uszqdkzh2a9d1r7pd0u.scalabl.cloud/1/functions/\""
            )
            buildConfigField(
                "String", "PARSE_APPLICATION_ID", "\"7ei5pxppEe8ErCMB6Ehm6HTIJRcdJJd7SRHUpN8n\""
            )
            buildConfigField(
                "String", "PARSE_REST_API_KEY", "\"rVEiA55uav4M5PbI3dYbWbJcIR67pf8JFIHRQNQn\""
            )
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            buildConfigField(
                "String", "APPLICATION_ID", "\"7ei5pxppEe8ErCMB6Ehm6HTIJRcdJJd7SRHUpN8n\""
            )
            buildConfigField("String", "CLIENT_KEY", "\"ko0v0w3Xl6jAZP5vhKxQj3HR7Ne8TcQJ7cHpJRIn\"")
            buildConfigField(
                "String",
                "URL",
                "\"https://pg-app-jtxlimrcv63uszqdkzh2a9d1r7pd0u.scalabl.cloud/1/\""
            )
            buildConfigField(
                "String",
                "BASE_URL",
                "\"https://pg-app-jtxlimrcv63uszqdkzh2a9d1r7pd0u.scalabl.cloud/1/functions/\""
            )
            buildConfigField(
                "String", "PARSE_APPLICATION_ID", "\"7ei5pxppEe8ErCMB6Ehm6HTIJRcdJJd7SRHUpN8n\""
            )
            buildConfigField(
                "String", "PARSE_REST_API_KEY", "\"rVEiA55uav4M5PbI3dYbWbJcIR67pf8JFIHRQNQn\""
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
        dataBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    // sdp ,ssp
    implementation(libs.ssp.android)
    implementation(libs.sdp.android)
    //otp
    implementation(libs.pinview)
    //viewpager2
    implementation(libs.androidx.viewpager2)
    //indicator
    implementation(libs.circleindicator)
    //datastore
    implementation(libs.androidx.datastore.preferences)
    // coroutine
    implementation(libs.kotlinx.coroutines.android)
    //parse
    implementation(libs.parse)
    //retrofit
    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.retrofit2.converter.scalars)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    // locations
    implementation(libs.play.services.location)
    //qr code
    implementation(libs.core)
    implementation(libs.zxing.android.embedded)
    //rounded image view
    implementation(libs.roundedimageview)
    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    // Fragment KTX
    implementation(libs.androidx.fragment.ktx)
    // Activity KTX
    implementation(libs.androidx.activity.ktx)
    //Toasty
    implementation(libs.toasty)
    //coil
    implementation(libs.coil)
    //shimmer
    implementation (libs.shimmer)
    implementation (libs.shimmer.recyclerview)

    //chucker
    debugImplementation("com.github.chuckerteam.chucker:library:3.5.2")
    releaseImplementation("com.github.chuckerteam.chucker:library-no-op:3.5.2")


}