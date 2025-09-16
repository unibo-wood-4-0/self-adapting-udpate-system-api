@file:Suppress("ktlint:standard:property-naming")

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("kotlin-mpp-convention")
}

kotlin {
    jvm {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_11)
                }
            }
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
            systemProperty("kotest.framework.testname.duplicate.mode", "Silent")
            filter {
                isFailOnNoMatchingTests = false
            }
        }
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                val `logback-classic` by catalogLibrary
                implementation(`logback-classic`)
            }
        }
        val jvmTest by getting {
            dependencies {
                val `kotest-jvm` by catalogBundle
                implementation(`kotest-jvm`)
            }
        }
    }
}
