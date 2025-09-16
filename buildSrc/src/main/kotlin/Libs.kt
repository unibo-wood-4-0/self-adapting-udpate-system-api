import org.gradle.api.Project

/**
 * A string reference to the [VersionCatalog] defined in `gradle/libs.versions.toml`.
 */
const val CATALOG_NAME = "libs"

/**
 * @return a reference to an os-auto-updates sub-project [module].
 */
fun Project.osAutoUpdates(module: String): Project = project(":os-auto-updates-$module")
