@file:Suppress("ktlint:standard:property-naming")

plugins {
    id("org.jetbrains.kotlin.multiplatform")
}

kotlin {
    targets.all {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    allWarningsAsErrors = false
                    freeCompilerArgs.addAll(
                        // TODO remove once Context Parameters are stable
                        "-Xcontext-parameters",
                        // TODO remove once excepted and actual classes are not in Beta
                        "-Xexpect-actual-classes",
                    )
                }
            }
        }
    }
    sourceSets {
        val commonTest by getting {
            dependencies {
                val `kotlinx-coroutine-core` by catalogLibrary
                val `kotlin-testing-common` by catalogBundle
                val `kotest-common` by catalogBundle
                implementation(`kotlinx-coroutine-core`)
                implementation(`kotlin-testing-common`)
                implementation(`kotest-common`)
            }
        }
    }
}

tasks.withType<Test>().configureEach {
    systemProperty("kotest.framework.testname.display.full.path", "true")
}
