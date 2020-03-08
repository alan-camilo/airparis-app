// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    val kotlinVersion by extra("1.3.70")
    repositories {
        google()
        jcenter()
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.6.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.jlleitschuh.gradle:ktlint-gradle:9.2.1")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

apply(plugin = "org.jlleitschuh.gradle.ktlint")

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
