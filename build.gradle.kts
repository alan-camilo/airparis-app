buildscript {
    val kotlinVersion by extra("1.3.70")
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
        maven("https://maven.fabric.io/public")
        maven("https://dl.bintray.com/florent37/maven")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.6.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.jlleitschuh.gradle:ktlint-gradle:9.2.1")
        classpath("com.google.gms:google-services:4.3.3")
        classpath("io.fabric.tools:gradle:1.31.2")
    }
}

apply(plugin = "org.jlleitschuh.gradle.ktlint")

allprojects {
    repositories {
        mavenCentral()
        google()
        jcenter()
        maven("https://dl.bintray.com/icerockdev/moko")
        maven("https://dl.bintray.com/florent37/maven")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
