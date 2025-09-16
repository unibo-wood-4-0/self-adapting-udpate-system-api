package it.unibo.osautoupdates.label

import it.unibo.osautoupdates.device.DSI

/**
 * An interface representing a label expression.
 * A label expression can be evaluated against a [DSI]
 * to determine if it matches the criteria defined by the expression.
 */
sealed interface LabelExpression {
    /**
     * Evaluates the label expression against the given [DSI].
     * @param dsi the device state information to evaluate against.
     * @return `true` if the expression matches the DSI, `false` otherwise.
     */
    fun evaluate(dsi: DSI): Boolean

    /**
     * Negates the label expression.
     * This operator allows for logical negation of the expression,
     */
    operator fun not(): LabelExpression = Not(this)

    /**
     * Combines two label expressions with a logical AND operation.
     * This operator allows for combining multiple conditions that must all be true.
     * @param other the other label expression to combine with.
     * @return a new label expression representing the logical AND of this and the other expression.
     */
    infix fun and(other: LabelExpression): LabelExpression = And(this, other)

    /**
     * Combines two label expressions with a logical OR operation.
     * This operator allows for combining multiple conditions where at least one must be true.
     * @param other the other label expression to combine with.
     * @return a new label expression representing the logical OR of this and the other expression.
     */
    infix fun or(other: LabelExpression): LabelExpression = Or(this, other)

    private data class Not(
        val expression: LabelExpression,
    ) : LabelExpression {
        override fun evaluate(dsi: DSI): Boolean = !expression.evaluate(dsi)
    }

    private data class And(
        val left: LabelExpression,
        val right: LabelExpression,
    ) : LabelExpression {
        override fun evaluate(dsi: DSI): Boolean = left.evaluate(dsi) && right.evaluate(dsi)
    }

    private data class Or(
        val left: LabelExpression,
        val right: LabelExpression,
    ) : LabelExpression {
        override fun evaluate(dsi: DSI): Boolean = left.evaluate(dsi) || right.evaluate(dsi)
    }
}
