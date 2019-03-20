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

    fun playAlphabeta(board: TicTacToeBoard, symbol: TicTacToeSquare): Int {
        val availablePositions = board.getAvailablePositions()
        if (availablePositions.isEmpty()) {
            throw IllegalStateException("The game is already over.")
        }
        return availablePositions
                .map { Pair(it, alphabeta(board.play(symbol, it), symbol, false, Int.MIN_VALUE, Int.MAX_VALUE)) }
                .maxBy { it.second }!!
                .first
    }

    private fun minimax(board: TicTacToeBoard, player: TicTacToeSquare, max: Boolean): Int {
        // Terminal node
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

    private fun alphabeta(board: TicTacToeBoard, player: TicTacToeSquare, maximize: Boolean, alpha: Int, beta: Int): Int {
        // Terminal node
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

        if (maximize) {
            var value = Int.MIN_VALUE
            var newAlpha = alpha
            for (position in board.getAvailablePositions()) {
                value = Integer.max(value, alphabeta(board.play(player, position), player, false, newAlpha, beta))
                newAlpha = Integer.max(newAlpha, value)
                if (newAlpha >= beta) {
                    break
                }
            }
            return value
        } else {
            var value = Int.MAX_VALUE
            var newBeta = beta
            for (position in board.getAvailablePositions()) {
                value = Integer.min(value, alphabeta(board.play(opponent, position), player, true, alpha, newBeta))
                newBeta = Integer.min(newBeta, value)
                if (alpha >= newBeta) {
                    break
                }
            }
            return value
        }
    }
}