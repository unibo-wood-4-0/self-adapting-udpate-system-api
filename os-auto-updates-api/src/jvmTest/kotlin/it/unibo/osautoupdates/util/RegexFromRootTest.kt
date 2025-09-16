package it.unibo.osautoupdates.util

import io.kotest.assertions.withClue
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

// TODO move to commonTest once https://github.com/JetBrains/kotlin/pull/5402 is merged
class RegexFromRootTest :
    ShouldSpec({
        /**
         * Build a regex and also validate it.
         */
        fun regex(
            root: String,
            matcher: String,
            expectedPattern: String,
        ): Regex {
            val regex = RegexFromRoot(root, matcher).build()
            regex.pattern shouldBe expectedPattern
            regex shouldBe expectedPattern.toRegex()
            return regex
        }

        fun Regex.shouldMatch(
            match: Boolean,
            vararg string: String,
        ) = string.forEach {
            withClue(
                "Expected regex $this to ${if (match) "match" else "not match"} string $it",
            ) {
                if (match) this.matches(it) else !this.matches(it)
            }
        }

        fun Regex.shouldNotMatch(vararg string: String) = shouldMatch(false, *string)

        fun Regex.shouldMatch(vararg string: String) = shouldMatch(true, *string)

        should("build a regex from simple strings") {
            with(regex("root", "matcher", """\Qroot\Ematcher""")) {
                shouldMatch("root/matcher")
                shouldNotMatch(
                    "a",
                    "root/matcher1",
                    "root/matcher2",
                    "root/matcher/file.txt2",
                )
            }
        }
        should("build a regex using backslash for windows") {
            with(regex("""C:\""", """Windows\\System..""", """\QC:\\EWindows\\System..""")) {
                shouldMatch(
                    "C:\\Windows\\System32",
                    """C:\Windows\System42"""",
                    "C:\\Windows\\Systemab",
                )
                shouldNotMatch(
                    "C:/Windows\\System32\\",
                    "C:\\Windows\\System32/file.txt",
                    "C:/Windows\\System32",
                    "C:/Windows/System/file.txt",
                )
            }
        }
        should("build a regex in linux environment") {
            with(regex("/home/user", """Documents\/.*""", """\Q/home/user\EDocuments\/.*""")) {
                shouldMatch(
                    "/home/user/Documents/file.txt",
                    "/home/user/Documents/another/file.txt",
                )
                shouldNotMatch(
                    "/home/user/Downloads/file.txt",
                    "/home/user/Documents",
                    "/home/user/Documents/",
                )
            }
        }
    })
