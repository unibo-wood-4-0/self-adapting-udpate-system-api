package it.unibo.osautoupdates.software.version

import io.kotest.property.Arb
import io.kotest.property.Exhaustive
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.string
import io.kotest.property.exhaustive.boolean

object ArbVersions {
    private val arbIntForVersion = { Arb.int(0..100) }

    val arbSemanticVersion =
        arbitrary {
            val major = arbIntForVersion().bind()
            val minor = arbIntForVersion().bind()
            val patch = arbIntForVersion().bind()
            SemanticVersion(major, minor, patch)
        }

    val arbTagVersion =
        arbitrary {
            TagVersion(Arb.string().bind())
        }

    val arbVersion =
        arbitrary {
            Arb
                .choice(
                    arbSemanticVersion,
                    arbTagVersion,
                    Arb.element(NotDefined),
                ).bind()
        }
}
