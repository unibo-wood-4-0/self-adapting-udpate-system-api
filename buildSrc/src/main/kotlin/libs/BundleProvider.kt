package libs

import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependencyBundle

class BundleProvider(
    project: Project,
) : CatalogProvider<ExternalModuleDependencyBundle>(project) {
    override fun providerStrategy(propertyName: String) = catalog.findBundle(propertyName)

    override fun all(): List<String> = catalog.bundleAliases
}
