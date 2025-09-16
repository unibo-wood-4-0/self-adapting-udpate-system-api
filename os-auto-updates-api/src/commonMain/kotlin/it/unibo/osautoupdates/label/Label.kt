package it.unibo.osautoupdates.label

/**
 * An interface representing a label.
 * @param T the type of the label.
 * @param V the value of the label.
 */
interface Label<T, V : Any?> : LabelExpression {
    /**
     * The type of the label.
     */
    val type: T

    /**
     * The name of the label.
     */
    val value: V
}
