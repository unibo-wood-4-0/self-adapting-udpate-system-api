import GitVersion.Companion.gitVersion
import libs.BundleProvider
import libs.LibraryProvider
import libs.VersionProvider
import org.gradle.api.Project

val Project.catalogLibrary get() = LibraryProvider(this)

val Project.catalogVersion get() = VersionProvider(this)

val Project.catalogBundle get() = BundleProvider(this)

val Project.buildSrc get() = provider { rootProject.layout.projectDirectory.dir("buildSrc") }

val Project.rootProjectBuild get() = provider { rootProject.layout.buildDirectory }

val Project.artifactName get() = provider { "${project.name.lowercase()}-${gitVersion().asWindowsCompliant()}" }
