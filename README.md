# NewsApplication

    Dependancies Used:-

    // Library for Retrofit for API Call
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'androidx.multidex:multidex:2.0.1'

    implementation 'androidx.appcompat:appcompat:1.4.1'
    implementation 'com.google.android.material:material:1.5.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.3'

    // Library for cardView
    implementation 'androidx.cardview:cardview:1.0.0'

    // Library for FireBase Authentication
    implementation 'com.google.firebase:firebase-auth'
    implementation 'com.google.firebase:firebase-common-ktx:20.1.1'

    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.3'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'

    // Also declare the dependency for the Google Play services library and specify its version
    implementation 'com.google.android.gms:play-services-auth:20.2.0'

    // Import the BoM for the Firebase platform
    implementation platform('com.google.firebase:firebase-bom:30.2.0')

    // Facebook Sdk
    implementation 'com.facebook.android:facebook-android-sdk:latest.release'

    // Glide Library for loading images from url
    implementation 'com.github.bumptech.glide:glide:4.12.0'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.12.0'
