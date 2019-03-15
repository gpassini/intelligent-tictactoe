package com.intelligenttictactoe.demo

enum class TicTacToeSquare(val symbol: String, val isPlayer: Boolean) {
    X("X", true),

    O("O", true),

    EMPTY(" ", false);

    fun isNotPlayer(): Boolean {
        return !isPlayer
    }
}