package it.unibo.osautoupdates.software.spec

import arrow.core.nel
import arrow.core.nonEmptyListOf
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import it.unibo.osautoupdates.ds.DoesNothingDS
import it.unibo.osautoupdates.ds.FailDS
import it.unibo.osautoupdates.ds.failureStrategy.InstallFailureStrategy
import it.unibo.osautoupdates.ds.failureStrategy.PostInstallFailureStrategy
import it.unibo.osautoupdates.ds.failureStrategy.PreInstallFailureStrategy
import it.unibo.osautoupdates.software.ResolvedSoftware
import it.unibo.osautoupdates.software.spec.SoftwareProducer.Companion.softwareSpecification
import it.unibo.osautoupdates.software.version.Version.Companion.semantic
import it.unibo.osautoupdates.system.oscommand.OsCommand

class SoftwareSpecificationTest :
    ShouldSpec({
        val boolean = true

        should("be able to build a Software just using the name and version") {
            val spec = softwareSpecification<Boolean>("name", semantic(1, 0, 0))
            spec.name shouldBe "name"
            spec.version shouldBe semantic(1, 0, 0)
            val dss = spec.dss(boolean)
            dss shouldBe DoesNothingDS.nel()
            spec.deps(boolean) shouldBe emptySet()
            spec.validation(boolean) shouldBe emptyList()
        }

        fun spec() =
            softwareSpecification<Boolean>("name", semantic(1, 0, 0)) {
                validationTests { b ->
                    if (b) {
                        +OsCommand("1")
                    } else {
                        +OsCommand("2")
                    }
                }
                validationTests { b ->
                    if (!b) {
                        +OsCommand("3")
                    } else {
                        +OsCommand("4")
                    }
                }
                validationBlock.size shouldBe 2
                dss { b ->
                    if (b) {
                        +FailDS(PreInstallFailureStrategy)
                    } else {
                        +FailDS(PostInstallFailureStrategy)
                    }
                }
                deploymentStrategies { _ ->
                    +FailDS(InstallFailureStrategy)
                }
                dependencies { b ->
                    if (b) {
                        +softwareSpecification<Boolean>("dep1", semantic(1, 0, 0))
                    } else {
                        +softwareSpecification<Boolean>("dep2", semantic(1, 0, 0))
                    }
                }
            }

        should("be able to add validation tests, dependencies and deployment strategies") {
            val deps = spec().deps(boolean)
            deps.size shouldBe 1
            val dep = deps.first()
            dep.apply {
                name shouldBe "dep1"
                version shouldBe semantic(1, 0, 0)
                validation(boolean) shouldBe emptyList()
                deps(boolean) shouldBe emptySet()
                dss(boolean) shouldBe DoesNothingDS.nel()
            }
            spec().dss(boolean) shouldBe
                nonEmptyListOf(
                    FailDS(PreInstallFailureStrategy),
                    FailDS(InstallFailureStrategy),
                )
            spec().validation(boolean) shouldBe listOf(OsCommand("1"), OsCommand("4"))
        }

        should("build a ResolvedSoftware") {
            val resolved = spec().buildSoftware(boolean)
            val expected =
                ResolvedSoftware(
                    name = "name",
                    validationTests = listOf(OsCommand("1"), OsCommand("4")),
                    deploymentStrategies =
                        nonEmptyListOf(
                            FailDS(PreInstallFailureStrategy),
                            FailDS(InstallFailureStrategy),
                        ),
                    dependencies =
                        setOf(
                            ResolvedSoftware(
                                name = "dep1",
                                validationTests = emptyList(),
                                deploymentStrategies = DoesNothingDS.nel(),
                                dependencies = emptySet(),
                                version = semantic(1, 0, 0),
                            ),
                        ),
                    version = semantic(1, 0, 0),
                )
            resolved.name shouldBe expected.name
            resolved.version shouldBe expected.version
            resolved.validationTests shouldBe expected.validationTests
            resolved.deploymentStrategies shouldBe expected.deploymentStrategies
            resolved.dependencies shouldBe expected.dependencies
        }
    })
