package com.intelligenttictactoe.demo

import com.intelligenttictactoe.demo.TicTacToeSquare.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

/**
 * Game logic implementation.
 */
@Service
class TicTacToeService @Autowired constructor(
        /**
         * Minimax algorithm player.
         */
        private val minimaxPlayer: TicTacToeMinimaxPlayer
) {
    private companion object {
        private val log = LoggerFactory.getLogger(TicTacToeService::class.java)
    }
    /**
     * `true` if it is the player X`s turn.
     */
    private var isXPlayerTurn = true

    /**
     * `true` if the game is over.
     */
    private var isGameOver = false

    /**
     * Ordered list of all past board states, the last element being the its current state.
     */
    private val history = mutableListOf(TicTacToeBoard())

    /**
     * Plays at a given position (from 0 to 8).
     */
    fun play(position: Int): TicTacToeBoard {
        if (isGameOver) {
            log.info("The game is over.")
            return this.history.last()
        }

        val xPosition = position % 3
        val yPosition = position / 3

        val currentPlayer: TicTacToeSquare = if (isXPlayerTurn) X else O
        val newBoard: TicTacToeBoard = history.last().play(currentPlayer, xPosition, yPosition)
        history.add(newBoard)

        isGameOver = verifyEndConditions()

        isXPlayerTurn = isXPlayerTurn.not()

        return newBoard
    }

    /**
     * Plays at a random position.
     */
    fun playRandom() {
        play(getAvailableRandomPosition())
    }

    /**
     * Plays at a position chosen by the minimax player.
     */
    fun playMinimax(): TicTacToeBoard {
        if (isGameOver) {
            System.out.println("The game is over.")
            return this.history.last()
        }

        val currentPlayer: TicTacToeSquare = if (isXPlayerTurn) X else O
        val currentBoard = history.last()
        return play(minimaxPlayer.play(currentBoard, currentPlayer))
    }

    /**
     * Plays at a position chosen by the alphabeta pruning player.
     */
    fun playAlphabeta(): TicTacToeBoard {
        if (isGameOver) {
            System.out.println("The game is over.")
            return this.history.last()
        }

        val currentPlayer: TicTacToeSquare = if (isXPlayerTurn) X else O
        val currentBoard = history.last()
        return play(minimaxPlayer.playAlphabeta(currentBoard, currentPlayer))
    }

    /**
     * Simulates n times a game between the Minimax and the Alphabeta pruning players.
     */
    fun simulate(gamesNumber: Int): String {
        var xWins = 0
        var oWins = 0
        var draws = 0
        for (i in 0 until gamesNumber) {
            when (playComplete()) {
                X -> xWins++
                O -> oWins++
                EMPTY -> draws++
            }
        }
        return "X Wins : $xWins | O Wins : $oWins | Draws : $draws"
    }

    /**
     * Restarts the game.
     */
    fun resetGame() {
        history.clear()
        history.add(TicTacToeBoard())
        isXPlayerTurn = true
        isGameOver = false
    }

    /**
     * Returns a representation of the current board state.
     */
    fun print(): String {
        return history.last().toString()
    }

    /**
     * Returns a random available position (from 0 to 8) from the current board.
     */
    private fun getAvailableRandomPosition(): Int {
        val availablePositions = history.last().getAvailablePositions()
        if (availablePositions.isEmpty()) {
            throw IllegalStateException("No available positions.")
        }
        Collections.shuffle(availablePositions)
        return availablePositions[0]
    }

    /**
     * Returns `true` if the game is over. Whether a player won, or its a draw.
     */
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

    /**
     * Plays a game between the Minimax and the Alphabeta pruning players until the end.
     *
     * Returns the winner, if any, or a draw.
     */
    private fun playComplete(): TicTacToeSquare {
        this.resetGame()
        while (!verifyEndConditions()) {
            if (isXPlayerTurn) {
                this.playMinimax()
            } else {
                this.playAlphabeta()
            }
        }
        return this.history.last().getWinner()
    }
}