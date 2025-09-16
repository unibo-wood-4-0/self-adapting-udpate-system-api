package it.unibo.osautoupdates.software.builder

import it.unibo.osautoupdates.software.spec.SoftwareProducer
import it.unibo.osautoupdates.software.version.Version

/**
 * Builder for [SoftwareProducer].
 * @param name the name of the software.
 */
@SoftwareBuilderMarker
data class SoftwareSpecificationBuilder<T>(
    override val name: String,
    private val version: Version,
) : SoftwareLikeBuilder<T, SoftwareProducer<T>>(name) {
    /**
     * The block that will assign validation tests.
     */
    val validationBlock: MutableList<(Validation<T, SoftwareProducer<T>>).(T) -> Unit> = mutableListOf()

    /**
     * The block that will assign the dependencies.
     */
    val depsBlock: MutableList<(Dependencies<T, SoftwareProducer<T>>).(T) -> Unit> = mutableListOf()

    /**
     * The block that will assign the deployment strategies.
     */
    val dssBlock: MutableList<(DeploymentStrategies<T, SoftwareProducer<T>>).(T) -> Unit> = mutableListOf()

    /**
     * Specify the validations to add to the [SoftwareProducer] base on a [T] instance.
     * @param lambda the lambda that specifies the validations from a [T] instance.
     */
    fun validationTests(lambda: @SoftwareBuilderMarker Validation<T, SoftwareProducer<T>>.(T) -> Unit) {
        validationBlock.add(lambda)
    }

    /**
     * Specify the dependencies to add to the [SoftwareProducer] base on a [T] instance.
     * @param lambda the lambda that specifies the dependencies from a [T] instance.
     */
    fun dependencies(lambda: @SoftwareBuilderMarker Dependencies<T, SoftwareProducer<T>>.(T) -> Unit) {
        depsBlock.add(lambda)
    }

    /**
     * Specify the deployment strategies to add to the [SoftwareProducer] base on a [T] instance.
     * @param lambda the lambda that specifies the deployment strategies from a [T] instance.
     */
    fun deploymentStrategies(lambda: @SoftwareBuilderMarker DeploymentStrategies<T, SoftwareProducer<T>>.(T) -> Unit) {
        dssBlock.add(lambda)
    }

    /**
     * Same as [deploymentStrategies].
     * @see deploymentStrategies
     */
    fun dss(lambda: @SoftwareBuilderMarker DeploymentStrategies<T, SoftwareProducer<T>>.(T) -> Unit) {
        deploymentStrategies(lambda)
    }

    /**
     * Assign the properties and return the [SoftwareProducer].
     */
    override fun build(): SoftwareProducer<T> =
        SoftwareProducer(
            name = name,
            version = version,
            validation = { t ->
                validationBlock.forEach { b -> b.invoke(Validation(this), t) }
                validationTests
            },
            dss = { t ->
                dssBlock.forEach { b -> b.invoke(DeploymentStrategies(this), t) }
                deploymentStrategiesAsNel
            },
            deps = { t ->
                depsBlock.forEach { b -> b.invoke(Dependencies(this), t) }
                dependencies.toSet()
            },
        )
}
