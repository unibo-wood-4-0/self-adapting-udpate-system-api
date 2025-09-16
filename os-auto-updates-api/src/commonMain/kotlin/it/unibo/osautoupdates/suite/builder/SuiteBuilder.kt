package it.unibo.osautoupdates.suite.builder

import it.unibo.osautoupdates.software.Software
import it.unibo.osautoupdates.suite.Suite

/**
 * Builder for the [Suite].
 */
abstract class SuiteBuilder<S : Software> {
    /**
     * The [S] to add to the suite.
     */
    protected val software: MutableSet<S> = mutableSetOf<S>()

    /**
     * Add the given [S] to the suite.
     */
    operator fun S.unaryPlus() {
        software.add(this)
    }

    /**
     * Build the [Suite].
     */
    fun build(): Suite<S> = SuiteImpl(software)

    private data class SuiteImpl<S : Software>(
        private val elements: Set<S>,
    ) : Suite<S>,
        Set<S> by elements

    companion object {
        /**
         * Utility function to build a [Suite] using a lambda.
         * @param lambda a lambda that applies the [SuiteBuilder] functions.
         * @return a [Suite] with the given parameters.
         */
        fun <S : Software> suite(lambda: SuiteBuilder<S>.() -> Unit = {}): Suite<S> =
            object : SuiteBuilder<S>() { }.apply { lambda() }.build()
    }
}
