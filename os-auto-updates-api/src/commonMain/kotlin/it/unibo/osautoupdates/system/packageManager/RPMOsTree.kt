package it.unibo.osautoupdates.system.packageManager

import it.unibo.osautoupdates.software.ResolvedSoftware
import it.unibo.osautoupdates.system.oscommand.OsCommand
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The dnf packet manager used in Fedora immutable OS.
 * @see <a href="https://coreos.github.io/rpm-ostree/">rpm-ostree</a>
 */
@Serializable
@SerialName(RPMOsTree.SERIAL_NAME)
internal data object RPMOsTree : PackageManager {
    override val baseCommand = OsCommand("rpm-ostree")

    private fun rpmOsTree(vararg args: String): OsCommand = OsCommand(baseCommand.commands + args.asList())

    override fun installCommand(software: ResolvedSoftware): OsCommand =
        rpmOsTree(
            "install",
            software.specification().asString(),
        )

    override fun uninstallCommand(software: ResolvedSoftware): OsCommand = rpmOsTree("remove", software.name)

    /**
     * The name of the [RPMOsTree] type, used for serialization.
     */
    internal const val SERIAL_NAME = "RpmOSTree"
}
