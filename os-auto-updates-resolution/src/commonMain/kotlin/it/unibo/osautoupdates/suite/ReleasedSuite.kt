package it.unibo.osautoupdates.suite

import it.unibo.osautoupdates.software.ReleasedSoftware
import it.unibo.osautoupdates.suite.builder.SuiteBuilder

/**
 * A [ReleasedSuite] is a [Suite] of [it.unibo.osautoupdates.software.ReleasedSoftware].
 */
typealias ReleasedSuite = Suite<ReleasedSoftware>

/**
 * Utility function to build a [ReleasedSuite] using a lambda.
 * @param lambda a lambda that applies the [SuiteBuilder] functions.
 * @return a [ReleasedSuite] with the given parameters.
 */
fun releasedSuite(lambda: SuiteBuilder<ReleasedSoftware>.() -> Unit = {}): ReleasedSuite = SuiteBuilder.suite(lambda)
