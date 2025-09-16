package it.unibo.osautoupdates.suite

import it.unibo.osautoupdates.ds.PackageManagerDS
import it.unibo.osautoupdates.serialization.suite.SuiteSerializer
import it.unibo.osautoupdates.software.ResolvedSoftware
import it.unibo.osautoupdates.software.Software
import it.unibo.osautoupdates.software.SoftwareExtensions.flatten
import it.unibo.osautoupdates.software.SoftwareName
import it.unibo.osautoupdates.suite.builder.SuiteBuilder
import it.unibo.osautoupdates.system.packageManager.PackageManager
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A [ResolvedSuite] is a [Suite] of [ResolvedSoftware].
 * It is used to represent a suite of software that has been resolved and is ready for deployment.
 */
typealias ResolvedSuite = Suite<ResolvedSoftware>

/**
 * A [Suite] is a set of [Software].
 */

@Serializable(with = SuiteSerializer::class)
@SerialName(Suite.SERIAL_NAME)
interface Suite<out S : Software> : Set<S> {
    /**
     * @return the [PackageManager]s required by the [Suite] to be deployed correctly.
     */
    fun requiredPackageManagers(): Set<PackageManager> =
        this
            .asSequence()
            .flatMap { sw ->
                sw.flatten()
            }.flatMap {
                it.deploymentStrategies
            }.filterIsInstance<PackageManagerDS>()
            .map {
                it.packageManager
            }.toSet()

    /**
     * Flatten the [Suite] into a set of [S] by calling the [S.flatten] method on each [Software].
     * @return the set of [S] that are in the [Suite].
     */
    fun flatten(): Set<S> = this.flatMap { it.flatten() }.toSet()

    /**
     * Retrieve the [SoftwareName]s of the [Software] in the [Suite].
     */
    fun flattenedNames(): Set<SoftwareName> = flatten().map { it.name }.toSet()

    /**
     * Retrieve the first [S] available from the [Suite] by its [SoftwareName]. Also search in dependencies.
     * @param softwareName the [SoftwareName] to search for.
     */
    fun software(softwareName: SoftwareName): S? = flatten().firstOrNull { it.name == softwareName }

    /**
     * Utility for the [Suite] class.
     */
    companion object {
        /**
         * Utility function to build a [ResolvedSuite] using a lambda.
         * @param lambda a lambda that applies the [SuiteBuilder] functions.
         * @return a [Suite] with the given parameters.
         */
        fun resolvedSuite(lambda: SuiteBuilder<ResolvedSoftware>.() -> Unit = {}): ResolvedSuite =
            SuiteBuilder.suite(lambda)

        /**
         * The name of the [Suite] type, used for serialization.
         */
        const val SERIAL_NAME = "Suite"
    }
}
