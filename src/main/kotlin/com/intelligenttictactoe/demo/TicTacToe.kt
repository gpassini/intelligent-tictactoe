package com.intelligenttictactoe.demo

import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import java.util.*
import java.util.stream.Collectors
import java.util.stream.IntStream
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@ShellComponent
class TicTacToe {

    companion object {
        const val EMPTY = " "
        const val X_PLAYER = "X"
        const val O_PLAYER = "O"
    }

    private final val board: Array<Array<String>> = arrayOf(
            arrayOf(EMPTY, EMPTY, EMPTY),
            arrayOf(EMPTY, EMPTY, EMPTY),
            arrayOf(EMPTY, EMPTY, EMPTY)
    )

    private var isXPlayerTurn = true
    private var isGameOver = false

    @ShellMethod(value = "Choose a position to play (0 to 8).", key = ["play", "p"])
    fun play(@Min(0) @Max(8) position: Int): String {
        doPlay(position)
        return print()
    }

    @ShellMethod(value = "Play randomly.", key = ["random", "rand", "r"])
    fun random(): String {
        doPlay(getAvailablePosition())
        return print()
    }

    private fun getAvailablePosition(): Int {
        val availablePositions = getAvailablePositions()
        if (availablePositions.isEmpty()) {
            throw IllegalStateException("No available positions.")
        }
        Collections.shuffle(availablePositions)
        return availablePositions[0]
    }

    private fun getAvailablePositions(): List<Int> {
        return IntStream.range(0, 9).filter { board[it % 3][it / 3] == EMPTY }.boxed().collect(Collectors.toList())
    }

    private fun doPlay(position: Int) {
        if (isGameOver) {
            System.out.println("The game is over.")
            return
        }
        val xPosition = position % 3
        val yPosition = position / 3

        verifyValidPosition(xPosition, yPosition)
        verifyAvailablePosition(xPosition, yPosition)

        board[xPosition][yPosition] = if (isXPlayerTurn) X_PLAYER else O_PLAYER

        isGameOver = verifyEndConditions()

        isXPlayerTurn = isXPlayerTurn.not()
    }

    private fun verifyAvailablePosition(xPosition: Int, yPosition: Int) {
        if (board[xPosition][yPosition] != EMPTY) {
            throw IllegalArgumentException("Position [x=$xPosition, y=$yPosition] is already taken.")
        }
    }

    private fun verifyValidPosition(xPosition: Int, yPosition: Int) {
        if (xPosition < 0 || xPosition > 2 || yPosition < 0 || yPosition > 2) {
            throw IllegalArgumentException("Impossible position (must be between 0 and 2) : [x=$xPosition, y=$yPosition].")
        }
    }

    private fun verifyEndConditions(): Boolean {
        // Verify diagonals
        if (board[0][0] != EMPTY && board[0][0] == board[1][1] && board[0][0] == board[2][2]
                || board[0][2] != EMPTY && board[0][2] == board[1][1] && board[0][2] == board[2][0]) {
            System.out.println("Player ${board[1][1]} won!")
            return true
        }

        // Verify verticals and horizontals
        for (i in 0 until 2) {
            val pivot = board[i][i]
            if (pivot != EMPTY
                    && (pivot == board[i][0] && pivot == board[i][1] && pivot == board[i][2]
                            || pivot == board[0][i] && pivot == board[1][i] && pivot == board[2][i])) {
                System.out.println("Player $pivot won!")
                return true
            }
        }

        return false
    }

    private fun print(): String {
        return "| ${board[0][0]} |  ${board[1][0]} |  ${board[2][0]} |\n" +
                "| ${board[0][1]} |  ${board[1][1]} |  ${board[2][1]} |\n" +
                "| ${board[0][2]} |  ${board[1][2]} |  ${board[2][2]} |"
    }
}