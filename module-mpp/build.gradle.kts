import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

val ktorVersion by extra("1.3.1")
val serializationVersion by extra("0.20.0")

buildscript {
    val kotlinVersion by extra("1.3.70")
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.6.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.3.70"
}

repositories {
    jcenter()
    mavenCentral()
}
group = "com.alancamilo.airparis"
version = "1.0"

kotlin {
    //select iOS target platform depending on the Xcode environment variables
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

    jvm("android")

    sourceSets["commonMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
        //Ktor
        implementation("io.ktor:ktor-client-core:$ktorVersion")
        implementation("io.ktor:ktor-client-json:$ktorVersion")
        implementation("io.ktor:ktor-client-serialization:$ktorVersion")
        //Kotlinx serialization
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:$serializationVersion")
    }

    sourceSets["commonTest"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-test-common")
        implementation("org.jetbrains.kotlin:kotlin-test-annotations-common")
    }

    sourceSets["androidMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib")
        //Ktor
        implementation("io.ktor:ktor-client-android:$ktorVersion")
        implementation("io.ktor:ktor-client-json-jvm:$ktorVersion")
        implementation("io.ktor:ktor-client-serialization-jvm:$ktorVersion")
        //Kotlinx serialization
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serializationVersion")
    }

    sourceSets["androidTest"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-test")
        implementation("org.jetbrains.kotlin:kotlin-test-junit")
    }

    sourceSets["iosMain"].dependencies {}

    sourceSets["iosTest"].dependencies {}
}

val packForXcode by tasks.creating(Sync::class) {
    val targetDir = File(buildDir, "xcode-frameworks")

    /// selecting the right configuration for the iOS
    /// framework depending on the environment
    /// variables set by Xcode build
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val framework = kotlin.targets
        .getByName<KotlinNativeTarget>("ios")
        .binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)

    from({ framework.outputDirectory })
    into(targetDir)

    /// generate a helpful ./gradlew wrapper with embedded Java path
    doLast {
        val gradlew = File(targetDir, "gradlew")
        gradlew.writeText("#!/bin/bash\n"
                + "export 'JAVA_HOME=${System.getProperty("java.home")}'\n"
                + "cd '${rootProject.rootDir}'\n"
                + "./gradlew \$@\n")
        gradlew.setExecutable(true)
    }
}

tasks.getByName("build").dependsOn(packForXcode)

tasks.withType<Test> {
    testLogging {
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }
}