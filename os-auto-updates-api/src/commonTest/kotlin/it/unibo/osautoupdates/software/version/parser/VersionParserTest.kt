package it.unibo.osautoupdates.software.version.parser

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Exhaustive
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.collection
import it.unibo.osautoupdates.software.version.ArbVersions.arbSemanticVersion
import it.unibo.osautoupdates.software.version.ArbVersions.arbTagVersion
import it.unibo.osautoupdates.software.version.SemanticVersion
import it.unibo.osautoupdates.software.version.TagVersion
import it.unibo.osautoupdates.software.version.Version

class VersionParserTest :
    ShouldSpec({
        val parser = VersionParser
        should("parse semantic versions correctly") {
            checkAll(
                Exhaustive.collection(setOf("v", "")),
                arbSemanticVersion,
            ) { v, version ->
                parser.parse("$v$version") shouldBe
                    Version.semantic(
                        version.major,
                        version.minor,
                        version.patch,
                    )
            }
        }
        should("parse tag versions correctly") {
            checkAll(arbTagVersion) { version ->
                parser.parse(version.toString()) shouldBe
                    Version.tag(version.toString())
            }
        }
        should("parse a string using a matching module and a specified defaultOperation that are added") {
            val mockModule =
                object : VersionParserModule {
                    override val regex = Regex("abc")
                    override val parseOperation: (String) -> Version = {
                        Version.semantic(1, 0, 0)
                    }
                }
            val parser = VersionParser
            parser.addAll(mockModule)
            val semantic = parser.parse("abc")
            semantic.shouldBeInstanceOf<SemanticVersion>()
            semantic shouldBe Version.semantic(1, 0, 0)
            val tag = parser.parse("cba")
            tag.shouldBeInstanceOf<TagVersion>()
            tag shouldBe TagVersion("cba")
        }
    })
