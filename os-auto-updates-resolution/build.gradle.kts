plugins {
    id("kotlin-jvm-convention")
    id("kotlin-js-convention")
}

kotlin {
    sourceSets {
        with(libs) {
            val commonMain by getting {
                dependencies {
                    implementation(osAutoUpdates("api"))
                    implementation(bundles.arrow)
                    implementation(libs.logging)
                    implementation(libs.kotlinx.serialization.json)
                    implementation(kotlinx.datetime)
                }
            }
            val jvmMain by getting {
                dependencies {
//                    implementation(bundles.ktor.server)
//                    implementation(bundles.ktor.server.jvm)
//                    implementation(bundles.mongodb)
//                    implementation(jackson.dataformat.yaml)
//                    implementation(snakeyaml)
//                    implementation(kotlinx.serialization.json)
                }
            }
        }
    }
}
