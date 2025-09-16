@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

plugins {
    id("com.gradle.develocity") version "3.17.6"
    id("org.danilopianini.gradle-pre-commit-git-hooks") version "1.1.18"
}

develocity {
    buildScan {
        termsOfUseUrl = "https://gradle.com/terms-of-service"
        termsOfUseAgree = "yes"
        publishing.onlyIf { it.buildResult.failures.isNotEmpty() }
    }
}

gitHooks {
    commitMsg { conventionalCommits() }
    preCommit {
        tasks("ktlintCheck", "detekt", "--parallel")
    }
    createHooks(overwriteExisting = true)
}

rootProject.name = "os-auto-updates"

listOf(
    "api",
    "resolution",
).forEach {
    include(":os-auto-updates-$it")
}
