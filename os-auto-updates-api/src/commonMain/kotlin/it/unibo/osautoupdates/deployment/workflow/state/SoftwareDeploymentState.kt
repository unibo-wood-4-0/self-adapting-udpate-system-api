package it.unibo.osautoupdates.deployment.workflow.state

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The dependencies of the software are being deployed.
 */
@Serializable
@SerialName("DeployDependencies")
data object DeployDependencies : State

/**
 * The software is being pre-installed using the pre-install commands.
 */
@Serializable
@SerialName("PreInstall")
data object PreInstall : State

/**
 * The software is being fetched from a source.
 */
@Serializable
@SerialName("Fetch")
data object Fetch : State

/**
 * The software is being validated using the validation commands.
 */
@Serializable
@SerialName("Validation")
data object Validation : State

/**
 * The software is being installed.
 */
@Serializable
@SerialName("Install")
data object Install : State

/**
 * The software is being post-installed using the post-install commands.
 */
@Serializable
@SerialName("PostInstall")
data object PostInstall : State

/**
 * The software is being uninstalled.
 */
@Serializable
@SerialName("Uninstall")
data object Uninstall : State

/**
 * The software is correctly deployed. This is a final state.
 */
@Serializable
@SerialName("Deployed")
data object Deployed : State.Final

/**
 * The software is not deployed. This is a final state.
 */
@Serializable
@SerialName("NotDeployed")
data object NotDeployed : State.Final
