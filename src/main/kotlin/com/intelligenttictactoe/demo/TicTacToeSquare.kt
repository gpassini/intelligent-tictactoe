package com.intelligenttictactoe.demo

/**
 * Possible states of a board space.
 */
enum class TicTacToeSquare(
        /**
         * String representation of the state.
         */
        val symbol: String,

        /**
         * `true` is it represents a player.
         */
        val isPlayer: Boolean) {
    /**
     * Player X.
     */
    X("X", true),

    /**
     * Player O.
     */
    O("O", true),

    /**
     * Empty space.
     */
    EMPTY(" ", false);
}