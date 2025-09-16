package it.unibo.osautoupdates.serialization.system.oscommand

import it.unibo.osautoupdates.serialization.common.StringSurrogateSerializer
import it.unibo.osautoupdates.system.oscommand.OsCommand

/**
 * Custom serializer for [OsCommand] that maps it to a string.
 */
object OsCommandStringSerializer : StringSurrogateSerializer<OsCommand>(
    OsCommand.SERIAL_NAME,
    decodeStringStrategy = { OsCommand.fromSingleString(it) },
)
