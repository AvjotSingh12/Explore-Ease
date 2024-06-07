plugins {
    id("com.android.application")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.travelapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.travelapp"
        minSdk = 26
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
    buildFeatures {
        buildConfig = true

    }
}
//dependencies {
//    implementation ("com.google.ar:core:1.43.0")
//
//    implementation ("com.google.ar.sceneform.ux:sceneform-ux:1.17.1")
//
//    implementation ("com.google.firebase:firebase-analytics")
//    implementation ("com.airbnb.android:lottie:6.4.0")
//    implementation ("com.google.android.libraries.places:places:3.4.0")
//    implementation("com.google.android.gms:play-services-cronet:18.0.1")
//    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
//    implementation("com.google.firebase:firebase-auth")
//    implementation("com.github.bumptech.glide:glide:4.16.0")
//    implementation("com.android.volley:volley:1.2.1")
//    implementation("com.google.android.material:material:1.12.0")
//    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
//    implementation("com.google.android.gms:play-services-maps:18.2.0")
//    implementation("androidx.appcompat:appcompat:1.6.1")
//    implementation("androidx.activity:activity:1.9.0")
//    testImplementation("junit:junit:4.13.2")
//    androidTestImplementation("androidx.test.ext:junit:1.1.5")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
//    implementation("com.google.android.libraries.places:places:3.4.0")
//    implementation("com.google.ai.client.generativeai:generativeai:0.6.0")
//    implementation("com.google.guava:guava:31.0.1-jre")
//    implementation("org.reactivestreams:reactive-streams:1.0.4")
//    implementation("androidx.concurrent:concurrent-futures:1.1.0")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-guava:1.7.3")
//    implementation("com.google.firebase:firebase-firestore")
//    implementation("com.google.firebase:firebase-storage")
//    implementation ("com.squareup.okhttp3:okhttp:4.11.0")
//    //implementation("com.squareup.okhttp3:ojhttp:4.7.2")
//    implementation ("com.google.firebase:firebase-database:21.0.0")
//    implementation ("com.google.code.gson:gson:2.10.1")
//    implementation ("androidx.recyclerview:recyclerview:1.3.2")
//}
    dependencies {
        implementation("com.google.ar:core:1.43.0")
        implementation("com.google.ar.sceneform.ux:sceneform-ux:1.17.1")

        implementation("com.google.firebase:firebase-analytics")
        implementation("com.airbnb.android:lottie:6.4.0")
        implementation("com.google.android.libraries.places:places:3.4.0")
        implementation("com.google.android.gms:play-services-cronet:18.0.1")
        implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
        implementation("com.google.firebase:firebase-auth")
        implementation("com.github.bumptech.glide:glide:4.16.0")
        implementation("com.android.volley:volley:1.2.1")
        implementation("com.google.android.material:material:1.12.0")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
        implementation("com.google.android.gms:play-services-maps:18.2.0")
        implementation("androidx.appcompat:appcompat:1.6.1")
        implementation("androidx.activity:activity:1.9.0")
        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
        implementation("com.google.ai.client.generativeai:generativeai:0.6.0")
        implementation("com.google.guava:guava:31.0.1-jre")
        implementation("org.reactivestreams:reactive-streams:1.0.4")
        implementation("androidx.concurrent:concurrent-futures:1.1.0")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-guava:1.7.3")
        implementation("com.google.firebase:firebase-firestore")
        implementation("com.google.firebase:firebase-storage")
        implementation("com.squareup.okhttp3:okhttp:4.11.0")
        implementation("com.google.firebase:firebase-database:21.0.0")
        implementation("com.google.code.gson:gson:2.10.1")
        implementation("androidx.recyclerview:recyclerview:1.3.2")
    }

