package it.unibo.osautoupdates.ds.artifact

import it.unibo.osautoupdates.system.oscommand.OsCommand
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(OperationSpecification.SERIAL_NAME)
data class OperationSpecification(
    val installCommands: List<OsCommand> = emptyList(),
    val uninstallCommands: List<OsCommand> = emptyList(),
) {
    companion object {
        const val SERIAL_NAME = "OperationSpecification"
    }
}
