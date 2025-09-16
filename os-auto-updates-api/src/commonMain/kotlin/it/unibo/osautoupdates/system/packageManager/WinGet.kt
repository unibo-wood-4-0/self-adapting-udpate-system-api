package it.unibo.osautoupdates.system.packageManager

import it.unibo.osautoupdates.software.ResolvedSoftware
import it.unibo.osautoupdates.software.version.NotDefined
import it.unibo.osautoupdates.software.version.Version
import it.unibo.osautoupdates.system.oscommand.OsCommand
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(WinGet.SERIAL_NAME)
internal data object WinGet : PackageManager {
    override val baseCommand: OsCommand = OsCommand("winget")

    private fun winget(vararg args: String): OsCommand = OsCommand(baseCommand.commands + (args.asList()))

    override fun installCommand(software: ResolvedSoftware): OsCommand =
        winget(
            "install",
            software.name,
            *versionSubCommand(software.version),
            "--force",
            "--accept-package-agreements",
            "--accept-source-agreements",
            "--disable-interactivity",
        )

    private fun versionSubCommand(version: Version): Array<String> =
        when (version) {
            NotDefined -> arrayOf()
            else -> arrayOf("--version", version.toString())
        }

    override fun uninstallCommand(software: ResolvedSoftware): OsCommand = winget("uninstall", software.name)

    /**
     * The name of the [WinGet] type, used for serialization.
     */
    internal const val SERIAL_NAME = "WinGet"

    /**
     * The code returned by [WinGet] when the software is already installed.
     * At the moment this information is unused, but I leave it here because... Come on, what the fuck is this?
     * What were they thinking? How is this even real? Am I even real? I am seriously questioning reality after seeing
     * something so criminal. If you google it You won't even find a single result. I am so done.
     */
    @Suppress("Unused")
    const val ALREADY_INSTALLED_CODE = -1978335189
}
