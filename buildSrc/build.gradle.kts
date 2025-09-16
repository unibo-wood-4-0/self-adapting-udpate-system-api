plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

// Additional configuration inside `settings.gradle.kts`
with(extensions.getByType<VersionCatalogsExtension>().named("libs")) {
    dependencies {
        implementation(findLibrary("jpackage-plugin").get())
        implementation(findLibrary("kotlin-multiplatform-plugin").get())
        implementation(findLibrary("js-plain-objects-plugin").get())
    }
}
