import de.aaschmid.gradle.plugins.cpd.Cpd
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("kotlin-jvm-convention")
    id("kotlin-js-convention")
}

kotlin {
    sourceSets {
        with(libs) {
            val commonMain by getting {
                dependencies {
                    implementation(bundles.arrow)
                    implementation(libs.logging)
                    implementation(libs.kotlinx.serialization.json)
                    implementation(kotlinx.datetime)
                }
            }
            val jvmMain by getting {
                dependencies {
                    implementation(bundles.ktor.server)
                    implementation(bundles.ktor.server.jvm)
                    implementation(bundles.mongodb)
                    implementation(jackson.dataformat.yaml)
                    implementation(snakeyaml)
                    implementation(kotlinx.serialization.json)
                }
            }
        }
    }
}

tasks.withType<Cpd>().configureEach {
    exclude("*DS.kt")
}
