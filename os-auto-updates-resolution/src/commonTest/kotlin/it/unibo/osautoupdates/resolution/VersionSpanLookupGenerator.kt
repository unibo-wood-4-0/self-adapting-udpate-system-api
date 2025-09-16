package it.unibo.osautoupdates.it.unibo.osautoupdates.resolution

import arrow.core.raise.either
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import it.unibo.osautoupdates.ds.DeploymentStrategy
import it.unibo.osautoupdates.resolution.api.VersionSpanLookupFactory
import it.unibo.osautoupdates.software.ReleasedSoftware
import it.unibo.osautoupdates.software.ReleasedSoftware.Companion.releasedSoftware
import it.unibo.osautoupdates.software.version.span.VersionSpan.Companion.rangeFromString
import it.unibo.osautoupdates.software.version.span.VersionSpan.Companion.singleFromString

class VersionSpanLookupGenerator :
    ShouldSpec({

        /**
         * Shortcut to not express a [DeploymentStrategy] everytime.
         */
        fun releasedSoftware(
            name: String,
            version: String,
            lambda: ReleasedSoftware.Builder.() -> Unit = {},
        ): ReleasedSoftware = releasedSoftware(name, singleFromString(version), lambda)

        should("correctly return an empty map for an empty list of software") {
            either {
                VersionSpanLookupFactory.fromSoftware(emptyList()) shouldBe emptyMap()
            }
        }
        should("generate the correct version from more than one software") {
            val sws =
                setOf(
                    releasedSoftware("A", "1.0.0"),
                    releasedSoftware("B", "1.0.0") {
                        dependencies {
                            +releasedSoftware("D", rangeFromString("1.0.0", "2.0.0", "2.0.0"))
                        }
                    },
                    releasedSoftware("C", rangeFromString("0.1.0", "1.5.0", "1.5.0")) {
                        dependencies {
                            +releasedSoftware("D", "1.0.0")
                        }
                    },
                )
            either {
                VersionSpanLookupFactory.fromSoftware(sws) shouldBe
                    mapOf(
                        "A" to singleFromString("1.0.0"),
                        "B" to singleFromString("1.0.0"),
                        "C" to rangeFromString("0.1.0", "1.5.0", "1.5.0"),
                        "D" to singleFromString("1.0.0"),
                    )
            }
        }
        should("generate the correct versions in a single complex Software") {
            val software =
                releasedSoftware("A", "1.0.0") {
                    dependencies {
                        +releasedSoftware("B", "2.0.0") {
                            dependencies {
                                +releasedSoftware("C", "2.0.0")
                                +releasedSoftware("D", rangeFromString("4.0.0", "5.0.0", "5.0.0")) {
                                    dependencies {
                                        +releasedSoftware("C", "2.0.0") {
                                            dependencies {
                                                +releasedSoftware("E", "1.0.0")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        +releasedSoftware("C", rangeFromString("1.0.0", "3.0.0", "3.0.0"))
                        +releasedSoftware("D", "4.0.0") {
                            dependencies {
                                +releasedSoftware("C", rangeFromString("0.1.0", "9.0.0", "0.1.0"))
                            }
                        }
                    }
                }
            either {
                VersionSpanLookupFactory.fromSoftware(listOf(software)) shouldBe
                    mapOf(
                        "A" to singleFromString("1.0.0"),
                        "B" to singleFromString("2.0.0"),
                        "C" to singleFromString("2.0.0"),
                        "D" to singleFromString("4.0.0"),
                        "E" to singleFromString("1.0.0"),
                    )
            }
        }
    })
