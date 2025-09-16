package it.unibo.osautoupdates.software.version.parser

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import it.unibo.osautoupdates.software.version.Version

class VersionParserModuleTest :
    ShouldSpec({
        should("create an working object using the factory create method") {
            val regex = Regex("\\d+\\.\\d+\\.\\d+")
            val parseOperation: (String) -> Version = { Version.semantic(1, 0, 0) }
            val module = VersionParserModule.create(regex, parseOperation)
            module.regex shouldBe regex
            module.parseOperation shouldBe parseOperation
            module.parseOperation("a") shouldBe Version.semantic(1, 0, 0)
        }
    })
