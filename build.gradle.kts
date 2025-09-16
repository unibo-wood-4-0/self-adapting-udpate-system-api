@file:Suppress("UnusedPrivateMember")
@file:OptIn(InternalKotlinGradlePluginApi::class)

import com.google.devtools.ksp.gradle.KspAATask
import de.aaschmid.gradle.plugins.cpd.Cpd
import io.gitlab.arturbosch.detekt.Detekt
import java.io.File.separator
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.InternalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.tasks.BaseKtLintCheckTask
import org.jlleitschuh.gradle.ktlint.tasks.KtLintCheckTask
import org.jlleitschuh.gradle.ktlint.tasks.KtLintFormatTask

plugins {
    alias(libs.plugins.kotest)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.dokka)
    alias(libs.plugins.gitSemVer)
    alias(libs.plugins.kotlin.qa)
    alias(libs.plugins.taskTree)
    alias(libs.plugins.kover)
    alias(libs.plugins.versions)
}

/**
 * Configuration common to all projects.
 */
allprojects {
    with(rootProject.libs.plugins) {
        apply(plugin = kotest.id)
        apply(plugin = ksp.id)
        apply(plugin = kotlin.serialization.id)
        apply(plugin = dokka.id)
        apply(plugin = gitSemVer.id)
        apply(plugin = kotlin.qa.id)
        apply(plugin = taskTree.id)
        apply(plugin = kover.id)
    }

    group = "it.unibo"

    repositories {
        google()
        mavenCentral()
    }

    fun PatternFilterable.excludeGenerated() = exclude { "build${separator}generated" in it.file.absolutePath }

    tasks {
        withType<Test>().configureEach {
            testLogging {
                showStandardStreams = true
                showExceptions = true
                exceptionFormat = TestExceptionFormat.FULL
            }
            setOf(
                "kotest.framework.testname.display.full.path" to true,
                "kotest.framework.classpath.scanning.autoscan.disable" to true,
                "kotest.framework.classpath.scanning.config.disable" to true,
            ).forEach { (property, value) ->
                systemProperty(property, value)
            }
        }
        withType<Detekt>().configureEach {
            excludeGenerated()
            enabled = false // Detekt is not used in this project for now, until context parameters are supported.
        }
        withType<KtLintCheckTask>().configureEach {
            excludeGenerated()
        }
        withType<BaseKtLintCheckTask>().configureEach {
            excludeGenerated()
        }
        withType<KtLintFormatTask>().configureEach {
            excludeGenerated()
        }
        withType<KspAATask>().configureEach {
            mustRunAfter(withType<Cpd>())
        }
    }
}

dependencies {
    rootProject.subprojects.forEach {
        kover(project(":${it.name}"))
    }
}

kover {
    reports {
        total {
            html { onCheck = true }
            xml { onCheck = true }
        }
    }
}
