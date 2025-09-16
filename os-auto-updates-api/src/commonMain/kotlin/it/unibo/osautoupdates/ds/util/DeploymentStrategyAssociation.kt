package it.unibo.osautoupdates.ds.util

import it.unibo.osautoupdates.ds.DeploymentStrategy
import it.unibo.osautoupdates.software.ResolvedSoftware

typealias DSA<DS> = DeploymentStrategyAssociation<DS>

/**
 * Represents a resolved software with an associated deployment strategy.
 * This class is used to encapsulate the software and its deployment strategy
 */
data class DeploymentStrategyAssociation<DS : DeploymentStrategy>(
    val software: ResolvedSoftware,
    val ds: DS,
) {
    companion object {
        /**
         * Creates a [DeploymentStrategyAssociation] instance with the given [ResolvedSoftware] and [DeploymentStrategy].
         * @param software the resolved software
         * @param ds the deployment strategy
         * @return a new instance of [DeploymentStrategyAssociation]
         */
        fun <DS : DeploymentStrategy> ResolvedSoftware.associate(ds: DS): DeploymentStrategyAssociation<DS> =
            DeploymentStrategyAssociation(this, ds)
    }
}
