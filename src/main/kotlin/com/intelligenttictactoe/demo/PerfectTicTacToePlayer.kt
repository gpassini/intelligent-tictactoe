package com.intelligenttictactoe.demo

class PerfectTicTacToePlayer(private val symbol: TicTacToeSquare) {

    init {
        if (!symbol.isNotPlayer()) {
            throw IllegalArgumentException("A player's symbol must be X ou Y.")
        }
    }


    fun play(board: TicTacToeBoard): Int {
        symbol
        return board.getAvailablePositions().first()
    }
}