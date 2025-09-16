package libs

import java.util.Optional
import kotlin.jvm.optionals.getOrNull
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionConstraint
import org.gradle.api.provider.Provider

class VersionProvider(
    val project: Project,
) : CatalogProvider<VersionConstraint>(project) {
    override fun providerStrategy(propertyName: String): Optional<Provider<VersionConstraint>> =
        Optional.of(
            project.provider { catalog.findVersion(propertyName).getOrNull() },
        )

    override fun all(): List<String> = catalog.versionAliases
}
