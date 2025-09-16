package it.unibo.osautoupdates.label

/**
 * An abstract implementation of the [Label] interface.
 * @param T the type of the label.
 * @param V the value of the label.
 */
abstract class AbstractLabel<T, V : Any?>(
    override val type: T,
    override val value: V,
) : Label<T, V> {
    override fun toString(): String = "$type:$value"
}
