package it.unibo.osautoupdates.util.logs

import io.github.oshai.kotlinlogging.Level

object LogLevel {
    fun supported(): Set<String> = Level.entries.map { it.name }.toSet()
}
