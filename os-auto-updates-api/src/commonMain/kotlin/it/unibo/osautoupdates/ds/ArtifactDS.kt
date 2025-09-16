package it.unibo.osautoupdates.ds

import it.unibo.osautoupdates.ds.artifact.FetchSpecification
import it.unibo.osautoupdates.ds.artifact.OperationSpecification
import it.unibo.osautoupdates.system.oscommand.OsCommand
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(ArtifactDS.SERIAL_NAME)
data class ArtifactDS(
    val fetch: FetchSpecification,
    val operations: OperationSpecification,
    override val preInstall: List<OsCommand> = emptyList(),
    override val postInstall: List<OsCommand> = emptyList(),
) : DeploymentStrategy {
    override val supportCache: Boolean get() = true

    companion object {
        const val SERIAL_NAME = "Artifact"
    }
}
