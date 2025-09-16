package it.unibo.osautoupdates.system.packageManager

import it.unibo.osautoupdates.software.ResolvedSoftware
import it.unibo.osautoupdates.system.oscommand.OsCommand
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Package manager to install from AUR on Arch-based Unix systems.
 */
@Serializable
@SerialName(Yay.SERIAL_NAME)
internal data object Yay : PackageManager {
    override val baseCommand: OsCommand = OsCommand("pamac")

    private fun pamac(vararg params: String): OsCommand = OsCommand(baseCommand.commands + params.asList())

    override fun installCommand(software: ResolvedSoftware): OsCommand =
        pamac(
            "install",
            "--no-confirm",
            software.specification().asString(),
        )

    override fun uninstallCommand(software: ResolvedSoftware): OsCommand =
        pamac(
            "remove",
            "--no-confirm",
            software.name,
        )

    /**
     * The name of the [Yay] type, used for serialization.
     */
    internal const val SERIAL_NAME = "Yay"
}
