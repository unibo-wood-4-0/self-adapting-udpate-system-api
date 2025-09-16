@file:UseSerializers(NonEmptyListSerializer::class)

package it.unibo.osautoupdates.software

import arrow.core.serialization.NonEmptyListSerializer
import it.unibo.osautoupdates.ds.DS
import it.unibo.osautoupdates.software.builder.SoftwareBuilderMarker
import it.unibo.osautoupdates.software.builder.SoftwareLikeBuilder
import it.unibo.osautoupdates.software.version.NotDefined
import it.unibo.osautoupdates.software.version.Version
import it.unibo.osautoupdates.system.oscommand.OsCommand
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

/**
 * A [it.unibo.osautoupdates.software.version.merger.span.SingleVersion] Software, created if there's no need to explicitly represent a range of compatibilities or
 * generated after the execution of [it.unibo.osautoupdates.software.version.resolution.ResolutionStrategy].
 * The latter selects one [it.unibo.osautoupdates.software.version.merger.span.SingleVersion] among a range of them, depending on other dependencies.
 * @param version the [Version] of the [ResolvedSoftware]. Default is [NotDefined].
 */
@Serializable
@SerialName(ResolvedSoftware.SERIAL_NAME)
data class ResolvedSoftware(
    override val name: String,
    override val validationTests: List<OsCommand> = emptyList(),
    override val deploymentStrategies: List<DS>,
    override val dependencies: Set<ResolvedSoftware> = emptySet(),
    override val version: Version = NotDefined,
) : VersionedSoftware() {
    /**
     * Builder for [ResolvedSoftware].
     * @param name the name of the software.
     * @param version the [Version] of the software. Default is [NotDefined].
     */
    @SoftwareBuilderMarker
    open class Builder(
        name: String,
        private var version: Version = NotDefined,
    ) : SoftwareLikeBuilder<Nothing, ResolvedSoftware>(name) {
        /**
         * @return a [ResolvedSoftware] with the given parameters.
         */
        override fun build(): ResolvedSoftware =
            ResolvedSoftware(
                name = name,
                validationTests = validationTests,
                deploymentStrategies = deploymentStrategiesAsNel,
                dependencies = dependencies,
                version = version,
            )
    }

    /**
     * Utility for the [ResolvedSoftware] class.
     */
    companion object {
        /**
         * The name of the [ResolvedSoftware] type, used for serialization.
         */
        const val SERIAL_NAME = "ResolvedSoftware"

        /**
         * Utility function to build a [ResolvedSoftware] using a lambda.
         * @param name the name of the software.
         * @param version the version of the software.
         * @param lambda the lambda that specifies the software.
         * @return the [ResolvedSoftware] built using the lambda.
         */

        fun resolvedSoftware(
            name: String,
            version: Version = NotDefined,
            lambda: Builder.() -> Unit = {},
        ): ResolvedSoftware = Builder(name, version).apply(lambda).build()

        /**
         * Utility function to build a [ResolvedSoftware] using a lambda.
         * @param name the name of the software.
         * @param version the version of the software.
         * @param lambda the lambda that specifies the software.
         * @return the [ResolvedSoftware] built using the lambda.
         */

        fun resolvedSoftware(
            name: String,
            version: String,
            lambda: Builder.() -> Unit = {},
        ): ResolvedSoftware = resolvedSoftware(name, Version.fromString(version), lambda)
    }
}
