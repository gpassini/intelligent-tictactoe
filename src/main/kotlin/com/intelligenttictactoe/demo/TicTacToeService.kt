package com.intelligenttictactoe.demo

import com.intelligenttictactoe.demo.TicTacToeSquare.O
import com.intelligenttictactoe.demo.TicTacToeSquare.X
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class TicTacToeService @Autowired constructor(private val minimaxPlayer: TicTacToeMinimaxPlayer) {

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

    fun playMinimax() {
        if (isGameOver) {
            System.out.println("The game is over.")
            return
        }

        val currentPlayer: TicTacToeSquare = if (isXPlayerTurn) X else O
        val currentBoard = history.last()
        return play(minimaxPlayer.play(currentBoard, currentPlayer))
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
        val board = history.last()

        val winner = board.getWinner()
        if (winner.isPlayer) {
            System.out.println("Player $winner won!")
            return true
        }

        if (board.isFull()) {
            System.out.println("Draw!")
            return true
        }

        return false
    }
}