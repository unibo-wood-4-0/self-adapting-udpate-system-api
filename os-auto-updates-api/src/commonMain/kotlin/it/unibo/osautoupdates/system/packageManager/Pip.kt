package it.unibo.osautoupdates.system.packageManager

import it.unibo.osautoupdates.software.ResolvedSoftware
import it.unibo.osautoupdates.system.oscommand.OsCommand
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The pip packet manager used for python packages.
 * @see <a href="https://pypi.org/project/pip/">pip</a>
 */
@Serializable
@SerialName(Pip.SERIAL_NAME)
internal data object Pip : PackageManager {
    override val baseCommand: OsCommand = OsCommand("pip")

    private fun pip(vararg args: String): OsCommand = OsCommand(baseCommand.commands + args.asList())

    override fun installCommand(software: ResolvedSoftware): OsCommand =
        pip(
            "install",
            software.specification().asString(separator = "=="),
        )

    override fun uninstallCommand(software: ResolvedSoftware): OsCommand = pip("uninstall", "-y", software.name)

    /**
     * The name of the [RPMOsTree] type, used for serialization.
     */
    internal const val SERIAL_NAME = "Pip"
}
