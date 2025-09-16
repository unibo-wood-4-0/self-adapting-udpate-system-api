package it.unibo.osautoupdates.label

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.Exhaustive
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.collection
import io.kotest.property.withAssumptions
import it.unibo.osautoupdates.device.DSI
import it.unibo.osautoupdates.device.DeviceId
import it.unibo.osautoupdates.label.implementations.DeviceIdLabel.Companion.deviceId
import it.unibo.osautoupdates.label.implementations.OsFamilyLabel.Companion.osFamily
import it.unibo.osautoupdates.system.os.Edition
import it.unibo.osautoupdates.system.os.Linux
import it.unibo.osautoupdates.system.os.Os
import it.unibo.osautoupdates.system.os.Windows

class TestLabelExpression :
    ShouldSpec({

        should("Test Label Expressions evaluation correctness") {
            val validId = "a"
            val dsi =
                DSI(
                    deviceId = DeviceId(validId),
                    os = Os(Windows, Edition("Windows 11 Pro")),
                    installedSoftware = emptyList(),
                )

            Arb.string().checkAll { id ->
                withAssumptions(id != validId) {
                    Exhaustive.collection(setOf(Windows, Linux)).checkAll { family ->
                        val andExpression = (osFamily(family) and deviceId(DeviceId(id)))
                        val orExpression = (osFamily(family) or deviceId(DeviceId(id)))
                        andExpression.evaluate(dsi) shouldBe false
                        orExpression.evaluate(dsi) shouldBe
                            when (family) {
                                Windows -> true
                                Linux -> false // Linux is not the OS family of the dsi
                            }
                    }
                }
            }
            osFamily(Linux).not().evaluate(dsi) shouldBe true
        }
    })
