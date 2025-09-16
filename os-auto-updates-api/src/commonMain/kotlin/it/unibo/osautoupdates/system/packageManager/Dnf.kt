package it.unibo.osautoupdates.system.packageManager

import it.unibo.osautoupdates.software.ResolvedSoftware
import it.unibo.osautoupdates.system.oscommand.OsCommand
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(Dnf.SERIAL_NAME)
internal data object Dnf : PackageManager {
    override val baseCommand: OsCommand = OsCommand("sudo", "dnf", "-y")

    private fun dnf(vararg args: String): OsCommand = OsCommand(baseCommand.commands + args.asList())

    override fun installCommand(software: ResolvedSoftware): OsCommand {
        PackageManager.warningForSpecifiedVersions(software, SERIAL_NAME)
        return dnf("install", software.name)
    }

    override fun uninstallCommand(software: ResolvedSoftware): OsCommand = dnf("remove", software.name)

    internal const val SERIAL_NAME = "Dnf"
}
