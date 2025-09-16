package it.unibo.osautoupdates.system.packageManager

import it.unibo.osautoupdates.software.ResolvedSoftware
import it.unibo.osautoupdates.system.oscommand.OsCommand
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(AptGet.SERIAL_NAME)
internal data object AptGet : PackageManager {
    override val baseCommand: OsCommand = OsCommand("sudo", "apt-get")

    private fun aptGet(vararg args: String): OsCommand = OsCommand(baseCommand.commands + args.asList())

    override fun installCommand(software: ResolvedSoftware): OsCommand =
        aptGet(
            "install",
            software.specification().asString(separator = "="),
        )

    override fun uninstallCommand(software: ResolvedSoftware): OsCommand = aptGet("uninstall", software.name)

    internal const val SERIAL_NAME = "AptGet"
}
