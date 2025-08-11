import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    alias(libs.plugins.kotlin.serialization)
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.easyEats.ezeats"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.easyEats.ezeats"
        minSdk = 26
        targetSdk = 36
        versionCode = 7
        versionName = "1.6"
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
            buildConfigField("String", "SECRET_KEY", "\"${project.findProperty("secret-key")}\"")

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
        signingConfigs {
            create("releaseConfig") {
                storeFile = file(rootProject.file("/ezeats_key/ezeats.jks"))
                storePassword = "ezeats"
                keyAlias = "ezeats"
                keyPassword = "ezeats"
            }
        }
        getByName("release") {
            isMinifyEnabled = false
            isShrinkResources = false
            signingConfig = signingConfigs.getByName("releaseConfig")
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
            buildConfigField("String", "SECRET_KEY", "\"${project.findProperty("secret-key")}\"")

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
        sourceCompatibility = JavaVersion.VERSION_19
        targetCompatibility = JavaVersion.VERSION_19
    }
    kotlinOptions {
        jvmTarget = "19"
    }

    buildFeatures {
        dataBinding = true
        buildConfig = true
        compose = true
    }

    // Tell the Android plugin which Compose compiler to use:
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }

    kotlinOptions {
        jvmTarget = "19"
        freeCompilerArgs += listOf(
            "-Xopt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-Xopt-in=kotlin.RequiresOptIn",
            "-Xjvm-default=all"
        )
    }
}

dependencies {

    // Compose dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.compose.icons)

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

//    implementation( libs.androidx.credentials.v150)
//    implementation (libs.androidx.credentials.play.services.auth)
//    implementation (libs.googleid)

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
    implementation( libs.play.services.maps)
    implementation(libs.maps.compose)

    //qr code
    implementation(libs.core)
    implementation(libs.zxing.android.embedded)

//    val room_version = "2.6.1"

//    implementation("androidx.room:room-runtime:$room_version")
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.room.compiler)

//
//    // If this project uses any Kotlin source, use Kotlin Symbol Processing (KSP)
//    // See Add the KSP plugin to your project
//    ksp("androidx.room:room-compiler:$room_version")
//
//    // If this project only uses Java source, use the Java annotationProcessor
//    // No additional plugins are necessary
//    annotationProcessor("androidx.room:room-compiler:$room_version")

    // optional - Kotlin Extensions and Coroutines support for Room
//    implementation("androidx.room:room-ktx:$room_version")

    //json encode
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3") // Use latest version

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
    implementation(libs.coil.svg)
    implementation(libs.coil.compose)
    implementation( "androidx.compose.runtime:runtime-livedata:1.5.4")

    //chucker
    debugImplementation(libs.library)
    releaseImplementation(libs.library.no.op)
    //firebase
    implementation(platform(libs.firebase.bom))
    implementation("com.google.firebase:firebase-auth-ktx:21.1.0")
    implementation("com.google.android.gms:play-services-auth:21.4.0")



    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.messaging.ktx)
    //mixpanel
    implementation(libs.mixpanel.android)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    // in app update
    implementation(libs.app.update.ktx)
    //lottie
    implementation(libs.lottie)
    // Google Sign-In
    implementation(libs.androidx.credentials)
    // Facebook Login
    implementation(libs.facebook.login)

    //Lottie
    implementation(libs.lottie.compose)


}