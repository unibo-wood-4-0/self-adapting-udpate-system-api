package libs

import CATALOG_NAME
import java.util.Optional
import kotlin.jvm.optionals.getOrNull
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType

abstract class CatalogProvider<T: Any>(
    project: Project,
) : ReadOnlyProperty<Any?, Provider<T>> {
    protected val catalog: VersionCatalog =
        project
            .extensions
            .getByType<VersionCatalogsExtension>()
            .named(CATALOG_NAME)

    abstract fun providerStrategy(propertyName: String): Optional<Provider<T>>

    abstract fun all(): List<String>

    override operator fun getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ): Provider<T> {
        val propertyName = property.name.replace(Regex("[A-Z]")) { "-${it.value.lowercase()}" }
        val value: Provider<T>? = providerStrategy(propertyName).getOrNull()
        requireNotNull(value) {
            "${property.name} not found in catalog: possible values are: ${
                all().joinToString(
                    separator = ", ",
                )
            }"
        }
        return value
    }
}
