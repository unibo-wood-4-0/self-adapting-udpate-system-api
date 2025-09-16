package it.unibo.osautoupdates.software.spec

import arrow.core.Nel
import arrow.core.nel
import it.unibo.osautoupdates.ds.DS
import it.unibo.osautoupdates.ds.DoesNothingDS
import it.unibo.osautoupdates.software.ResolvedSoftware
import it.unibo.osautoupdates.software.builder.SoftwareSpecificationBuilder
import it.unibo.osautoupdates.software.version.Version
import it.unibo.osautoupdates.system.oscommand.OsCommand

/**
 * A specification for a software.
 * The name and the version are simply chosen, the other fields are chosen based on [T].
 * @param name the name of the software.
 * @param version the version of the software.
 * @param validation the validation tests to run on the software.
 * @param dss the deployment strategies to run on the software.
 * @param deps the dependencies of the software.
 */
data class SoftwareProducer<in T>(
    val name: String,
    val version: Version,
    val validation: (T) -> List<OsCommand> = { emptyList() },
    val dss: (T) -> Nel<DS> = { DoesNothingDS.nel() },
    val deps: (T) -> Set<SoftwareProducer<T>> = { emptySet() },
) {
    /**
     * Utility for the [SoftwareProducer] class.
     */
    companion object {
        /**
         * Create a Software Specification using a lambda to define the customizable properties.
         * @param name the name of the software.
         * @param version the version of the software.
         * @param builder the lambda to define the customizable properties.
         */
        fun <T> softwareSpecification(
            name: String,
            version: Version,
            builder: SoftwareSpecificationBuilder<T>.() -> Unit = {},
        ): SoftwareProducer<T> = SoftwareSpecificationBuilder<T>(name, version).apply(builder).build()
    }

    /**
     * Build a [ResolvedSoftware] using the [T] instance.
     * @param t the instance of [T] to use to build the [ResolvedSoftware].
     * @return the [ResolvedSoftware] built using the [T] instance.
     */
    fun buildSoftware(t: T): ResolvedSoftware =
        ResolvedSoftware(
            name = name,
            validationTests = validation(t),
            deploymentStrategies = dss(t),
            dependencies = deps(t).map { it.buildSoftware(t) }.toSet(),
            version = version,
        )
}
