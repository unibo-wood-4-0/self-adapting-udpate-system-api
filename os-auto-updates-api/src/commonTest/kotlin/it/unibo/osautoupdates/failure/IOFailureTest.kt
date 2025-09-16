package it.unibo.osautoupdates.failure

import arrow.core.nonEmptyListOf
import arrow.core.nonEmptySetOf
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class IOFailureTest :
    ShouldSpec({
        val fileSystemFailure = IOFailure.FileSystemInteraction("oh no")
        val notFoundFailure = IOFailure.NotFound(nonEmptyListOf("res0"))
        val unprocessableContentFailure = IOFailure.UnprocessableContent("reason")
        val repositoryInteractionFailure = IOFailure.RepositoryInteraction("reason")

        should("correctly print the message") {
            fileSystemFailure.message shouldContain "oh no"
            notFoundFailure.message shouldContain "res0"
            unprocessableContentFailure.message shouldContain "reason"
            repositoryInteractionFailure.message shouldContain "reason"
        }

        should("have different output for 1 or more resources that are not found") {
            IOFailure.NotFound(nonEmptyListOf("res0")).message shouldBe
                "Resource could not be found at the following location: `res0`"
            IOFailure.NotFound(nonEmptyListOf("res0", "res1")).message shouldBe
                "Resources could not be found at the following locations: `res0, res1`"
        }
        should("print correctly the message of invalid content") {
            IOFailure.UnprocessableContent("reason").message shouldBe
                "Resource could not be read because of its invalid content: `reason`"
        }
    })
