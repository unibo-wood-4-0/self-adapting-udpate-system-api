package it.unibo.osautoupdates.migration.migration

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import it.unibo.osautoupdates.migration.MoveFile
import it.unibo.osautoupdates.migration.resource.FileResource

class MoveFileTest :
    ShouldSpec({
        val moveFile = MoveFile(FileResource("source"), FileResource("destination"))
        should("be reversible") {
            val reversed = moveFile.reverse()
            reversed.shouldBeInstanceOf<MoveFile>()
            reversed.source shouldBe moveFile.destination
            reversed.destination shouldBe moveFile.source
        }
    })
