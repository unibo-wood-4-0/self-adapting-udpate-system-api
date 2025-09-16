package it.unibo.osautoupdates.resolution.api

import it.unibo.osautoupdates.software.SoftwareName
import it.unibo.osautoupdates.software.version.span.VersionSpan

/**
 * A Lookup map that maps a [SoftwareName] (the [Software] name) to a single precise [it.unibo.osautoupdates.software.version.merger.span.VersionSpan].
 * This map is used during the resolution process to select the correct [it.unibo.osautoupdates.software.version.merger.span.VersionSpan] for each [Software].
 */
typealias VersionSpanLookup = Map<SoftwareName, VersionSpan>
