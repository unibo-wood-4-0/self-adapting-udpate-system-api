package it.unibo.osautoupdates.it.unibo.osautoupdates.resolution.software.version

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import it.unibo.osautoupdates.software.version.Version.Companion.semantic
import it.unibo.osautoupdates.software.version.Version.Companion.tag
import it.unibo.osautoupdates.software.version.span.RangeVersion
import it.unibo.osautoupdates.software.version.span.SingleVersion
import it.unibo.osautoupdates.software.version.span.VersionSpan

class VersionSpanTest :
    ShouldSpec({

        val singleVersion = VersionSpan.single(semantic(1, 0, 0))

        should("have the correct properties") {
            when (singleVersion) {
                is SingleVersion -> {
                    singleVersion.version shouldBe semantic(1, 0, 0)
                }
                is RangeVersion<*> -> error("$singleVersion has been found to be a RangeVersion, this is impossible.")
            }
        }

        val minVersion = semantic(1, 0, 0)
        val maxVersion = semantic(2, 0, 2)
        val preferredVersion = semantic(1, 1, 0)
        val incompatibilities =
            setOf(
                semantic(1, 0, 1),
                semantic(1, 0, 2),
            )
        val rangeVersion =
            VersionSpan.range(
                minVersion,
                maxVersion,
                preferredVersion,
                incompatibilities,
            )

        val versionSpanSerializer = VersionSpan.serializer()

        should("have the correct properties") {
            when (rangeVersion) {
                is RangeVersion<*> -> {
                    rangeVersion.min shouldBe minVersion
                    rangeVersion.max shouldBe maxVersion
                    rangeVersion.preferred shouldBe preferredVersion
                    rangeVersion.incompatibilities shouldBe incompatibilities
                }
                is SingleVersion -> error("$rangeVersion has been found to be a SingleVersion, this is impossible.")
            }
        }
        should("throw IllegalArgumentException when assigning versions with different types") {
            shouldThrow<IllegalArgumentException> {
                VersionSpan.range(
                    tag("latest"),
                    semantic(2, 0, 0),
                    semantic(1, 0, 0),
                    setOf(semantic(1, 0, 0)),
                )
            }
            shouldThrow<IllegalArgumentException> {
                VersionSpan.range(
                    semantic(1, 0, 0),
                    tag("latest"),
                    semantic(1, 0, 0),
                    setOf(semantic(1, 0, 0)),
                )
            }
            shouldThrow<IllegalArgumentException> {
                VersionSpan.range(
                    semantic(1, 0, 0),
                    semantic(2, 0, 0),
                    semantic(1, 0, 0),
                    setOf(tag("latest")),
                )
            }
        }
        should("throw IllegalArgumentException when assigning a preferred outside of range") {
            shouldThrow<IllegalArgumentException> {
                VersionSpan.range(minVersion, maxVersion, semantic(3, 1, 0), setOf())
            }
            shouldThrow<IllegalArgumentException> {
                VersionSpan.range(minVersion, maxVersion, semantic(0, 1, 0), setOf())
            }
        }
        should("throw IllegalArgumentException when assigning a max greater than min") {
            shouldThrow<IllegalArgumentException> {
                VersionSpan.range(
                    semantic(3, 0, 0),
                    semantic(1, 0, 0),
                    preferredVersion,
                    setOf(),
                )
            }
        }
        should("throw IllegalArgumentException when assigning a max equal to min") {
            shouldThrow<IllegalArgumentException> {
                VersionSpan.range(
                    semantic(1, 0, 0),
                    semantic(1, 0, 0),
                    semantic(1, 0, 0),
                    setOf(),
                )
            }
        }
        should("throw IllegalArgumentException when assigning incompatible incompatibilities") {
            shouldThrow<IllegalArgumentException> {
                VersionSpan.range(
                    minVersion,
                    maxVersion,
                    preferredVersion,
                    setOf(semantic(3, 1, 0)),
                )
            }
            shouldThrow<IllegalArgumentException> {
                VersionSpan.range(
                    minVersion,
                    maxVersion,
                    preferredVersion,
                    setOf(
                        semantic(0, 1, 0),
                        semantic(0, 0, 1),
                    ),
                )
            }
            shouldThrow<IllegalArgumentException> {
                // Same as minimum
                VersionSpan.range(
                    minVersion,
                    maxVersion,
                    preferredVersion,
                    setOf(semantic(1, 0, 0)),
                )
            }
            shouldThrow<IllegalArgumentException> {
                // Same as maximum
                VersionSpan.range(
                    minVersion,
                    maxVersion,
                    preferredVersion,
                    setOf(semantic(2, 0, 2)),
                )
            }
            shouldThrow<IllegalArgumentException> {
                // Same as preferred
                VersionSpan.range(
                    minVersion,
                    maxVersion,
                    preferredVersion,
                    setOf(semantic(1, 1, 0)),
                )
            }
        }
    })
