package it.unibo.osautoupdates.util

import it.unibo.osautoupdates.serialization.deployment.ObjectIDToStringSerializer
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * An identifier composed of a [String].
 * @property value The identifier.
 */
@JvmInline
@Serializable(with = ObjectIDToStringSerializer::class)
value class StringId(
    val value: String,
) {
    /**
     * Converts the [StringId] to a [String].
     * @return the [String] representation of the [StringId].
     */
    override fun toString(): String = value
}
