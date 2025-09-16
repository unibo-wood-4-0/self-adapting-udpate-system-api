package it.unibo.osautoupdates.serialization.suite

import it.unibo.osautoupdates.software.Software
import it.unibo.osautoupdates.suite.Suite
import it.unibo.osautoupdates.suite.builder.SuiteBuilder.Companion.suite
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class SuiteSerializer<S : Software>(
    softwareSerializer: KSerializer<S>,
) : KSerializer<Suite<S>> {
    private val listSerializer = ListSerializer(softwareSerializer)

    override val descriptor: SerialDescriptor = listSerializer.descriptor

    override fun deserialize(decoder: Decoder): Suite<S> {
        val softwareList = listSerializer.deserialize(decoder)
        return suite {
            softwareList.forEach { +it }
        }
    }

    override fun serialize(
        encoder: Encoder,
        value: Suite<S>,
    ) = listSerializer.serialize(encoder, value.toList())
}
