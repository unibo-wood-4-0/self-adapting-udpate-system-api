package it.unibo.osautoupdates.it.unibo.osautoupdates.resolution.merger

import arrow.core.left
import arrow.core.right
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import it.unibo.osautoupdates.failure.ResolutionFailure
import it.unibo.osautoupdates.software.version.Version
import it.unibo.osautoupdates.software.version.Version.Companion.semantic
import it.unibo.osautoupdates.software.version.Version.Companion.toVersion
import it.unibo.osautoupdates.software.version.span.RangeVersion
import it.unibo.osautoupdates.software.version.span.SingleVersion
import it.unibo.osautoupdates.software.version.span.VersionSpan
import it.unibo.osautoupdates.software.version.span.VersionSpan.Companion.rangeFromString
import it.unibo.osautoupdates.software.version.span.VersionSpan.Companion.singleFromString

@Suppress("UNCHECKED_CAST")
class VersionSpanMergerTest :
    ShouldSpec({

        should("return NoElementToResolve when given < 1 parameters") {
            VersionSpan.mergeAll() shouldBe ResolutionFailure.NoElementToResolve.left()
        }

        should("return the same version when given exactly 1 parameter") {
            VersionSpan.mergeAll(singleFromString("1.0.0")) shouldBe singleFromString("1.0.0").right()
            val vr = rangeFromString("1.0.0", "2.0.0", "1.9.0")
            VersionSpan.mergeAll(vr) shouldBe vr.right()
        }

        should("return the same SingleVersion when given the same SingleVersion lot of times") {
            VersionSpan.mergeAll(
                singleFromString("1.0.0"),
                singleFromString("1.0.0"),
            ) shouldBe singleFromString("1.0.0").right()
            VersionSpan.mergeAll(
                singleFromString("1.0.0"),
                singleFromString("1.0.0"),
                singleFromString("1.0.0"),
            ) shouldBe singleFromString("1.0.0").right()
        }

        should("return null when passing incompatible SingleVersions") {
            VersionSpan.mergeAll(singleFromString("1.0.0"), singleFromString("2.0.0")) shouldBe
                ResolutionFailure
                    .IncompatibleSingleVersions(
                        singleFromString("1.0.0") as SingleVersion,
                        singleFromString("2.0.0") as SingleVersion,
                    ).left()
            VersionSpan.mergeAll(
                singleFromString("2.0.0"),
                singleFromString("2.0.0"),
                singleFromString("3.0.0"),
            ) shouldBe
                ResolutionFailure
                    .IncompatibleSingleVersions(
                        singleFromString("2.0.0") as SingleVersion,
                        singleFromString("3.0.0") as SingleVersion,
                    ).left()
            VersionSpan.mergeAll(
                singleFromString("4.0.0"),
                singleFromString("0.1.0"),
                singleFromString("0.1.0"),
            ) shouldBe
                ResolutionFailure
                    .IncompatibleSingleVersions(
                        singleFromString("4.0.0") as SingleVersion,
                        singleFromString("0.1.0") as SingleVersion,
                    ).left()
        }

        should("return the SingleVersion when also given and a RangeVersion that contains it") {
            val vr = rangeFromString("1.0.0", "2.0.0", "1.9.0")
            VersionSpan.mergeAll(singleFromString("1.0.0"), vr) shouldBe singleFromString("1.0.0").right()
            VersionSpan.mergeAll(singleFromString("1.9.0"), vr) shouldBe singleFromString("1.9.0").right()
            VersionSpan.mergeAll(singleFromString("2.0.0"), vr) shouldBe singleFromString("2.0.0").right()
        }

        should("return null when given a SingleVersion and a RangeVersion that does not contain it") {
            val vr: RangeVersion<Version> = rangeFromString("1.0.0", "2.0.0", "1.9.0") as RangeVersion<Version>
            VersionSpan.mergeAll(singleFromString("0.9.4"), vr) shouldBe
                ResolutionFailure
                    .SingleVersionOutOfRange(
                        singleFromString("0.9.4") as SingleVersion,
                        vr,
                    ).left()
            VersionSpan.mergeAll(singleFromString("3.0.0"), vr) shouldBe
                ResolutionFailure
                    .SingleVersionOutOfRange(
                        singleFromString("3.0.0") as SingleVersion,
                        vr,
                    ).left()
            val vrExclusions: RangeVersion<Version> =
                rangeFromString(
                    "1.0.0",
                    "2.0.0",
                    "2.0.0",
                    setOf("1.9.0", "1.9.1"),
                ) as RangeVersion<Version>
            VersionSpan.mergeAll(singleFromString("1.9.0"), vrExclusions) shouldBe
                ResolutionFailure
                    .SingleVersionOutOfRange(
                        singleFromString("1.9.0") as SingleVersion,
                        vrExclusions,
                    ).left()
            VersionSpan.mergeAll(singleFromString("1.9.1"), vrExclusions) shouldBe
                ResolutionFailure
                    .SingleVersionOutOfRange(
                        singleFromString("1.9.1") as SingleVersion,
                        vrExclusions,
                    ).left()
        }

        should("a merged Version when given compatible SingleVersion to a RangeVersion") {
            val vr = rangeFromString("1.0.0", "2.0.0", "1.9.0")
            listOf("1.0.0", "1.9.0", "2.0.0").forEach {
                VersionSpan.mergeAll(
                    vr,
                    VersionSpan.single(it.toVersion()),
                ) shouldBe VersionSpan.single(it.toVersion()).right()
            }
        }

        should("return the same RangeVersion when given two RangeVersions that are equals") {
            val vr1 = rangeFromString("1.0.0", "2.0.0", "1.9.0")
            val vr2 = rangeFromString("1.0.0", "2.0.0", "1.9.0")
            VersionSpan.mergeAll(vr1, vr2) shouldBe vr1.right()
            VersionSpan.mergeAll(vr2, vr1) shouldBe vr2.right()
        }

        should("return the intersection of the RangeVersion when given two compatible RangeVersions") {
            val vr1 = rangeFromString("0.1.0", "2.0.0", "2.0.0", setOf("1.6.0", "1.5.0"))
            val vr2 = rangeFromString("1.0.0", "3.0.0", "1.9.0", setOf("1.7.0", "1.5.0"))
            VersionSpan.mergeAll(vr1, vr2) shouldBe
                rangeFromString(
                    "1.0.0",
                    "2.0.0",
                    "2.0.0",
                    setOf("1.6.0", "1.5.0", "1.7.0"),
                ).right()
        }

        /**
         * What happens here is that the only possible compatible range is [1.0.0, 2.0.0], but since both
         * version are in the merged incompatible set, the only version left is 1.9.0 which is also the preferred.
         */
        should("return the preferred version when excluding max and min from range") {
            val vr1 = rangeFromString("0.1.0", "2.0.0", "2.0.0", setOf("1.0.0"))
            val vr2 = rangeFromString("1.0.0", "3.0.0", "1.9.0", setOf("2.0.0", "1.5.0"))
            VersionSpan.mergeAll(vr1, vr2) shouldBe
                VersionSpan
                    .single(semantic(1, 9, 0))
                    .right()
        }

        /**
         * What happens here is that the only possible compatible range is [1.0.0, 2.0.0], but since 1.0.0
         * is in the merged incompatible set, the only two versions left are 1.9.0 and 2.0.0.
         * Consequently, 1.9.0 is set as the preferred and min version, while 2.0.0 is set as the max version.
         */
        should("merge two RangeVersion using the preferred version as min if unavailable") {
            val vr1 = rangeFromString("0.1.0", "2.0.0", "0.4.0", setOf("1.0.0"))
            val vr2 = rangeFromString("1.0.0", "3.0.0", "1.9.0")
            VersionSpan.mergeAll(vr1, vr2) shouldBe rangeFromString("1.9.0", "2.0.0", "1.9.0").right()
        }

        /**
         * What happens here is that the only possible compatible range is [1.0.0, 2.0.0], but since 2.0.0
         * is in the merged incompatible set, the only two versions left are 1.0.0 and 1.9.0.
         * Consequently, 1.9.0 is set as the preferred and max version, while 1.0.0 is set as the min version.
         */
        should("merge two RangeVersion using the preferred version as max if unavailable") {
            val vr1 = rangeFromString("0.1.0", "2.0.0", "2.0.0")
            val vr2 = rangeFromString("1.0.0", "3.0.0", "1.9.0", setOf("2.0.0"))
            VersionSpan.mergeAll(vr1, vr2) shouldBe rangeFromString("1.0.0", "1.9.0", "1.9.0").right()
        }
    })
