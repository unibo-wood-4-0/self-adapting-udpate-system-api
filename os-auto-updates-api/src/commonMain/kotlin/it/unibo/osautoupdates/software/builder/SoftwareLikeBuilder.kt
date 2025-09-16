package it.unibo.osautoupdates.software.builder

import arrow.core.Nel
import arrow.core.nel
import arrow.core.toNonEmptyListOrNull
import it.unibo.osautoupdates.ds.DS
import it.unibo.osautoupdates.ds.DeploymentStrategy
import it.unibo.osautoupdates.ds.DoesNothingDS
import it.unibo.osautoupdates.software.ResolvedSoftware
import it.unibo.osautoupdates.software.Software
import it.unibo.osautoupdates.system.oscommand.OsCommand

/**
 * Marker for the [SoftwareLikeBuilder] class.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
@DslMarker
annotation class SoftwareBuilderMarker

/**
 * Builder for [Software].
 * Can be used as a starting point for other builders related to [Software].
 * @param name the name of the software.
 */
@SoftwareBuilderMarker
abstract class SoftwareLikeBuilder<F, S>(
    protected open val name: String,
) {
    /**
     * The [DeploymentStrategy]s to run on the [S].
     */
    protected var deploymentStrategies = mutableListOf<DeploymentStrategy>()

    /**
     * The [DeploymentStrategy]s to run on the [S] but as a [Nel].
     */
    protected val deploymentStrategiesAsNel by lazy {
        deploymentStrategies.toNonEmptyListOrNull() ?: DoesNothingDS.nel()
    }

    /**
     * The validation tests to run on the [S].
     */
    protected val validationTests = mutableListOf<OsCommand>()

    /**
     * The dependencies of the [S].
     */
    protected val dependencies = mutableSetOf<S>()

    /**
     * Inner class to specify the validations to add to the [ResolvedSoftware].
     * @param builder the [SoftwareLikeBuilder] to add the validations to.
     */
    class Validation<F, S>(
        private val builder: SoftwareLikeBuilder<F, S>,
    ) {
        /**
         * Add a validation test to the [ResolvedSoftware].
         */
        operator fun OsCommand.unaryPlus() {
            builder.validationTests.add(this)
        }
    }

    /**
     * Specify the validations to add to the [ResolvedSoftware].
     * @param lambda a lambda that applies the [MutableList] functions.
     */
    fun validationTests(lambda: @SoftwareBuilderMarker Validation<F, S>.() -> Unit) {
        lambda.invoke(Validation(this))
    }

    /**
     * Inner class to specify the dependencies to add to the [ResolvedSoftware].
     * @param builder the [SoftwareLikeBuilder] to add the validations to.
     */
    class Dependencies<F, S>(
        private val builder: SoftwareLikeBuilder<F, S>,
    ) {
        /**
         * Add a validation test to the [ResolvedSoftware].
         */
        operator fun S.unaryPlus() {
            builder.dependencies.add(this)
        }
    }

    /**
     * Specify the dependencies to add to the [ResolvedSoftware].
     * @param lambda a lambda that applies the [MutableSet] functions.
     */
    fun dependencies(lambda: (@SoftwareBuilderMarker Dependencies<F, S>).() -> Unit) {
        lambda.invoke(Dependencies(this))
    }

    /**
     * Specify the [DS]s to add to the [ResolvedSoftware].
     */
    class DeploymentStrategies<F, S>(
        private val builder: SoftwareLikeBuilder<F, S>,
    ) {
        /**
         * Add a [DS] to the [ResolvedSoftware].
         */
        operator fun DS.unaryPlus() {
            builder.deploymentStrategies.add(this)
        }
    }

    /**
     * Specify the [DS]s to add to the [ResolvedSoftware].
     */
    fun deploymentStrategies(lambda: (@SoftwareBuilderMarker DeploymentStrategies<F, S>).() -> Unit) {
        lambda.invoke(DeploymentStrategies(this))
    }

    /**
     * Specify the [DS]s to add to the [ResolvedSoftware].
     */
    fun dss(lambda: (@SoftwareBuilderMarker DeploymentStrategies<F, S>).() -> Unit) {
        deploymentStrategies(lambda)
    }

    /**
     * @return a [S] with the given parameters.
     * The [S] is built using the [build] function.
     */
    abstract fun build(): S
}
