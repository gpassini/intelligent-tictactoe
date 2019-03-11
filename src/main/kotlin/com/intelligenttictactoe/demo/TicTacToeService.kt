package com.intelligenttictactoe.demo

import com.intelligenttictactoe.demo.TicTacToeSquare.*
import org.springframework.stereotype.Service
import java.util.*

@Service
class TicTacToeService {

    private var isXPlayerTurn = true
    private var isGameOver = false

    private val history = mutableListOf(TicTacToeBoard())

    fun play(position: Int) {
        if (isGameOver) {
            System.out.println("The game is over.")
            return
        }
        val xPosition = position % 3
        val yPosition = position / 3

        val currentPlayer: TicTacToeSquare = if (isXPlayerTurn) X else O
        val newBoard: TicTacToeBoard = history.last().play(currentPlayer, xPosition, yPosition)
        history.add(newBoard)

        isGameOver = verifyEndConditions()

        isXPlayerTurn = isXPlayerTurn.not()
    }

    fun playRandom() {
        play(getAvailableRandomPosition())
    }

    fun resetGame() {
        history.clear()
        history.add(TicTacToeBoard())
        isXPlayerTurn = true
        isGameOver = false
    }

    fun print(): String {
        return history.last().toString()
    }

    private fun getAvailableRandomPosition(): Int {
        val availablePositions = history.last().getAvailablePositions()
        if (availablePositions.isEmpty()) {
            throw IllegalStateException("No available positions.")
        }
        Collections.shuffle(availablePositions)
        return availablePositions[0]
    }

    private fun verifyEndConditions(): Boolean {
        val tictactoeBoard = history.last()
        val board = tictactoeBoard.board
        // Verify diagonals
        val center = board[1][1]
        if (center != EMPTY &&
                (center == board[0][0] && center == board[2][2]
                        || center == board[0][2] && center == board[2][0])) {
            System.out.println("Player $center won!")
            return true
        }

        // Verify verticals and horizontals
        for (i in 0 until 3) {
            val pivot = board[i][i]
            if (pivot != EMPTY
                    && (pivot == board[i][0] && pivot == board[i][1] && pivot == board[i][2]
                            || pivot == board[0][i] && pivot == board[1][i] && pivot == board[2][i])) {
                System.out.println("Player $pivot won!")
                return true
            }
        }

        // Verify draw
        if (tictactoeBoard.isFull()) {
            System.out.println("Draw!")
            return true
        }

        return false
    }
}