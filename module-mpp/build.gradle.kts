import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktorVersion by extra("1.3.2")
val serializerVersion by extra("0.20.0")
val kotlinVersion by extra("1.3.70")
val klockVersion by extra("1.10.0")

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization") version "1.3.70"
    id("org.jlleitschuh.gradle.ktlint")
}

android {
    compileSdkVersion(29)
    buildToolsVersion("29.0.2")

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(29)
    }

    // By default the android gradle plugin expects to find the kotlin source files in
    // the folder `main` and the test in the folder `test`. This is to be able place
    // the source code files inside androidMain and androidTest folders
    sourceSets {
        getByName("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            java.srcDirs(file("src/androidMain/kotlin"))
            res.srcDirs(file("src/androidMain/res"))
        }
        getByName("test") {
            java.srcDirs(file("src/androidTest/kotlin"))
            res.srcDirs(file("src/androidTest/res"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

kotlin {
    // select iOS target platform depending on the Xcode environment variables
    val iOSTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
            ::iosArm64
        else
            ::iosX64

    iOSTarget("ios") {
        binaries {
            framework {
                baseName = "SharedCode"
            }
        }
    }

    targets {
        targetFromPreset(presets.getByName("android"), "android")
    }

    sourceSets["commonMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
        // Ktor
        implementation("io.ktor:ktor-client-core:$ktorVersion")
        implementation("io.ktor:ktor-client-json:$ktorVersion")
        implementation("io.ktor:ktor-client-serialization:$ktorVersion")
        // Kotlinx serialization
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:$serializerVersion")
        // Date and time lib Klock
        implementation("com.soywiz.korlibs.klock:klock:$klockVersion")
        //MVVM
        implementation("dev.icerock.moko:mvvm:0.6.0")
    }

    sourceSets["commonTest"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-test-common")
        implementation("org.jetbrains.kotlin:kotlin-test-annotations-common")
        api("io.ktor:ktor-client-mock:$ktorVersion")
        // Date and time lib Klock
        implementation("com.soywiz.korlibs.klock:klock:$klockVersion")
    }

    sourceSets["androidMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib")
        // Ktor
        implementation("io.ktor:ktor-client-android:$ktorVersion")
        implementation("io.ktor:ktor-client-json-jvm:$ktorVersion")
        implementation("io.ktor:ktor-client-serialization-jvm:$ktorVersion")
        // Kotlinx serialization
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serializerVersion")
        //MVVM
        implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    }

    sourceSets["androidTest"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-test")
        implementation("org.jetbrains.kotlin:kotlin-test-junit")
        api("io.ktor:ktor-client-mock-jvm:$ktorVersion")
    }

    sourceSets["iosMain"].dependencies {
        // Ktor
        implementation("io.ktor:ktor-client-ios:$ktorVersion")
        implementation("io.ktor:ktor-client-json-native:$ktorVersion")
        implementation("io.ktor:ktor-client-serialization-native:$ktorVersion")
        // Kotlinx serializer
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:$serializerVersion")
    }

    sourceSets["iosTest"].dependencies {
        api("io.ktor:ktor-client-mock:$ktorVersion")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:1.3.4")
    }
}

val packForXcode by tasks.creating(Sync::class) {
    val targetDir = File(buildDir, "xcode-frameworks")

    // / selecting the right configuration for the iOS
    // / framework depending on the environment
    // / variables set by Xcode build
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val framework = kotlin.targets
        .getByName<KotlinNativeTarget>("ios")
        .binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)

    from({ framework.outputDirectory })
    into(targetDir)

    // / generate a helpful ./gradlew wrapper with embedded Java path
    doLast {
        val gradlew = File(targetDir, "gradlew")
        gradlew.writeText(
            "#!/bin/bash\n" +
                    "export 'JAVA_HOME=${System.getProperty("java.home")}'\n" +
                    "cd '${rootProject.rootDir}'\n" +
                    "./gradlew \$@\n"
        )
        gradlew.setExecutable(true)
    }
}

tasks.getByName("build").dependsOn(packForXcode)

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.withType<Test> {
    testLogging {
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }
}

ktlint {
    disabledRules.set(setOf("no-wildcard-imports"))
}
