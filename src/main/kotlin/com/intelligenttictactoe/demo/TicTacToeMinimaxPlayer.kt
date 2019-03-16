package com.intelligenttictactoe.demo

import com.intelligenttictactoe.demo.TicTacToeSquare.O
import com.intelligenttictactoe.demo.TicTacToeSquare.X
import org.springframework.stereotype.Service

/**
 * Implementation of the Minimax Algorithm applied to TicTacToe.
 */
@Service
class TicTacToeMinimaxPlayer {

    companion object {
        const val WIN = 10
        const val LOSE = -10
        const val DRAW = 0
    }

    fun play(board: TicTacToeBoard, symbol: TicTacToeSquare): Int {
        val availablePositions = board.getAvailablePositions()
        if (availablePositions.isEmpty()) {
            throw IllegalStateException("The game is already over.")
        }
        return availablePositions
                .map { Pair(it, minimax(board.play(symbol, it), symbol, false)) }
                .maxBy { it.second }!!
                .first
    }

    private fun minimax(board: TicTacToeBoard, player: TicTacToeSquare, max: Boolean): Int {
        val winner = board.getWinner()
        if (winner == player) {
            return WIN
        }

        val opponent = if (player == X) O else X
        if (winner == opponent) {
            return LOSE
        }

        if (board.isFull()) {
            return DRAW
        }

        val nextPlayer = if (max) player else opponent
        val values = board.getAvailablePositions().map { minimax(board.play(nextPlayer, it), player, max.not()) }

        return if (max) {
            values.max()!!
        } else {
            values.min()!!
        }
    }
}