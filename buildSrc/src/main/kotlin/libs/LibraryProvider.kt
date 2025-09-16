package libs

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency

class LibraryProvider(
    project: Project,
) : CatalogProvider<MinimalExternalModuleDependency>(project) {
    override fun providerStrategy(propertyName: String) = catalog.findLibrary(propertyName)

    override fun all(): List<String> = catalog.libraryAliases
}
