package it.unibo.osautoupdates.software

import it.unibo.osautoupdates.ds.DeploymentStrategy
import it.unibo.osautoupdates.serialization.software.DefaultReleasedSoftwareSerializer
import it.unibo.osautoupdates.software.builder.SoftwareBuilderMarker
import it.unibo.osautoupdates.software.builder.SoftwareLikeBuilder
import it.unibo.osautoupdates.software.version.NotDefined
import it.unibo.osautoupdates.software.version.span.SingleVersion
import it.unibo.osautoupdates.software.version.span.VersionSpan
import it.unibo.osautoupdates.system.oscommand.OsCommand
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule

/**
 * A type of [Software] with [it.unibo.osautoupdates.software.version.Version] version type.
 * It describes a [Software] when released by the developers.
 * @param versionSpan the range of versions that this [ReleasedSoftware] supports.
 */
@Serializable
@SerialName(ReleasedSoftware.SERIAL_NAME)
data class ReleasedSoftware(
    override val name: String,
    override val validationTests: List<OsCommand> = emptyList(),
    override val deploymentStrategies: List<DeploymentStrategy>,
    override val dependencies: Set<ReleasedSoftware> = emptySet(),
    val versionSpan: VersionSpan = SingleVersion(NotDefined),
) : Software {
    /**
     * Builder for [Builder].
     * @param name the name of the software.
     * @param versionSpan the [it.unibo.osautoupdates.software.version.merger.span.VersionSpan] of the software.
     */
    @SoftwareBuilderMarker
    open class Builder(
        name: String,
        private var versionSpan: VersionSpan,
    ) : SoftwareLikeBuilder<Nothing, ReleasedSoftware>(name) {
        /**
         * @return a [ReleasedSoftware] with the given parameters.
         */
        override fun build(): ReleasedSoftware =
            ReleasedSoftware(name, validationTests, deploymentStrategiesAsNel, dependencies, versionSpan)
    }

    /**
     * Utility for the [ReleasedSoftware] class.
     */
    companion object {
        /**
         * The name of the [ReleasedSoftware] type, used for serialization.
         */
        const val SERIAL_NAME = "ReleasedSoftware"

        /**
         * Utility function to build a [ReleasedSoftware] using a lambda.
         * @param name the name of the software.
         * @param versionSpan the [it.unibo.osautoupdates.software.version.merger.span.VersionSpan] of the software.
         * @param lambda the lambda that specifies the software.
         * @return the [ResolvedSoftware] built using the lambda.
         */
        fun releasedSoftware(
            name: String,
            versionSpan: VersionSpan = VersionSpan.single(NotDefined),
            lambda: Builder.() -> Unit = {},
        ): ReleasedSoftware = Builder(name, versionSpan).apply(lambda).build()

        /**
         * Serializer module for [ReleasedSoftware].
         */

        fun serializerModule() =
            SerializersModule {
                polymorphic(
                    baseClass = ReleasedSoftware::class,
                    actualClass = ReleasedSoftware::class,
                    actualSerializer = DefaultReleasedSoftwareSerializer,
                )
            }
    }
}
