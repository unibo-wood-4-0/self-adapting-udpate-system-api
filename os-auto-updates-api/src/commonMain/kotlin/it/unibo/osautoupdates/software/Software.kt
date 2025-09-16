package it.unibo.osautoupdates.software

import arrow.core.Nel
import it.unibo.osautoupdates.ds.DeploymentStrategy
import it.unibo.osautoupdates.software.version.Version
import it.unibo.osautoupdates.system.oscommand.OsCommand
import kotlinx.serialization.SerialName
import kotlinx.serialization.modules.SerializersModule

typealias SoftwareName = String

/**
 * The main entity of the model domain.
 * A piece of software is described with a name, and a [Version].
 * Moreover, each [Software] is equipped with validation tests, and some [DeploymentStrategy].
 * Both a container and an operating system are described as a [Software].
 */
@SerialName(Software.SERIAL_NAME)
interface Software {
    /**
     * @return the name of the [Software].
     */
    val name: String

    /**
     * @return the validation tests of the [Software].
     */
    val validationTests: List<OsCommand>

    /**
     * @return the [Nel] of [DeploymentStrategy] for the [Software] in priority order.
     */
    val deploymentStrategies: List<DeploymentStrategy>

    /**
     * A Set of [Software] dependencies that needs to be included to make this software work.
     */
    val dependencies: Set<Software>

    /**
     * Utility for the [Software] class.
     */
    companion object {
        fun serializerModule() =
            SerializersModule {
                polymorphic(
                    baseClass = ResolvedSoftware::class,
                    actualClass = ResolvedSoftware::class,
                    actualSerializer = ResolvedSoftware.serializer(),
                )
            }

        /**
         * The name of the [Software] type, used for serialization.
         */
        const val SERIAL_NAME = "Software"
    }
}
