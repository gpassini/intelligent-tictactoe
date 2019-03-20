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

    fun play(board: TicTacToeBoard, symbol: TicTacToeSquare, pruning: Boolean): Int {
        val availablePositions = board.getAvailablePositions()
        if (availablePositions.isEmpty()) {
            throw IllegalStateException("The game is already over.")
        }
        val mapper = if (pruning) {
            position: Int -> startAlphabeta(board, symbol, position)
        } else {
            position: Int -> startMinimax(board, symbol, position)
        }
        return availablePositions
                .shuffled()
                .map { Pair(it, mapper(it)) }
                .maxBy { it.second }!!
                .first
    }

    private fun startMinimax(board: TicTacToeBoard, symbol: TicTacToeSquare, position: Int) =
            minimax(board.play(symbol, position), symbol, maximize = false)

    private fun startAlphabeta(board: TicTacToeBoard, symbol: TicTacToeSquare, position: Int) =
            alphabeta(board.play(symbol, position), symbol, maximize = true, alpha = Int.MIN_VALUE, beta = Int.MAX_VALUE)

    private fun minimax(board: TicTacToeBoard, player: TicTacToeSquare, maximize: Boolean): Int {
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

        val nextPlayer = if (maximize) player else opponent
        val values = board.getAvailablePositions().map { minimax(board.play(nextPlayer, it), player, maximize.not()) }

        return if (maximize) {
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