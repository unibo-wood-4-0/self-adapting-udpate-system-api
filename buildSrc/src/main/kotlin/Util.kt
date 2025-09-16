import org.gradle.api.provider.Provider
import org.gradle.plugin.use.PluginDependency

/**
 * Directly accesses the plugin id.
 * @return the plugin id.
 */
val Provider<PluginDependency>.id: String get() = get().pluginId
