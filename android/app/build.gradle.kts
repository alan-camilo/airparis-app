val work_version = "2.3.4"

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("org.jlleitschuh.gradle.ktlint")
}

ktlint {
    disabledRules.set(setOf("no-wildcard-imports"))
}

android {
    compileSdkVersion(29)
    buildToolsVersion("29.0.2")

    defaultConfig {
        applicationId = "fr.paris-respire"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    dataBinding {
        isEnabled = true
    }

    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/license.txt")
        exclude("META-INF/NOTICE")
        exclude("META-INF/NOTICE.txt")
        exclude("META-INF/notice.txt")
        exclude("META-INF/ASL2.0")
        exclude("META-INF/*.kotlin_module")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(kotlin("stdlib-jdk8", "1.3.70"))
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.core:core-ktx:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
    implementation(project(":module-mpp"))
    implementation("com.codesgood:justifiedtextview:1.1.0")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("com.google.android.material:material:1.1.0")
    // WorkManager
    implementation("androidx.work:work-runtime-ktx:$work_version")
    androidTestImplementation("androidx.work:work-testing:$work_version")
    // Hyperion debug tool
    /*debugImplementation("com.willowtreeapps.hyperion:hyperion-core:0.9.27")
    debugImplementation("com.willowtreeapps.hyperion:hyperion-attr:0.9.27")
    debugImplementation("com.willowtreeapps.hyperion:hyperion-build-config:0.9.27")
    debugImplementation("com.willowtreeapps.hyperion:hyperion-crash:0.9.27")
    debugImplementation("com.willowtreeapps.hyperion:hyperion-disk:0.9.27")
    debugImplementation("com.willowtreeapps.hyperion:hyperion-measurement:0.9.27")
    debugImplementation("com.willowtreeapps.hyperion:hyperion-phoenix:0.9.27")
    debugImplementation("com.willowtreeapps.hyperion:hyperion-recorder:0.9.27")
    debugImplementation("com.willowtreeapps.hyperion:hyperion-shared-preferences:0.9.27")*/
    // TODO delete this dependency when coroutines will be in the module
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.4")
    // Joda time
    implementation("joda-time:joda-time:2.10.5")
}
