package com.intelligenttictactoe.demo

import com.intelligenttictactoe.demo.TicTacToeSquare.EMPTY
import java.util.stream.Collectors
import java.util.stream.IntStream

class TicTacToeBoard(private val board: List<List<TicTacToeSquare>> = createEmpty()) {

    private companion object {
        @JvmStatic
        private fun createEmpty(): List<List<TicTacToeSquare>> {
            return listOf(
                    listOf(EMPTY, EMPTY, EMPTY),
                    listOf(EMPTY, EMPTY, EMPTY),
                    listOf(EMPTY, EMPTY, EMPTY))
        }
    }

    fun play(player: TicTacToeSquare, position: Int): TicTacToeBoard {
        return createNew(player, position % 3, position / 3)
    }

    fun play(player: TicTacToeSquare, x: Int, y: Int): TicTacToeBoard {
        if (!player.isPlayer) {
            throw IllegalArgumentException("$player is not a player.")
        }
        if (x < 0 || x > 2 || y < 0 || y > 2) {
            throw IllegalArgumentException("Invalid position (must be between 0 and 2) : [x=$x, y=$y].")
        }
        if (board[x][y] != EMPTY) {
            throw IllegalArgumentException("Position [x=$x, y=$y] is already occupied.")
        }
        return createNew(player, x, y)
    }

    fun getAvailablePositions(): List<Int> {
        return IntStream
                .range(0, 9)
                .filter { board[it % 3][it / 3] == EMPTY }
                .boxed()
                .collect(Collectors.toList())
    }

    fun isFull(): Boolean {
        return IntStream
                .range(0, 9)
                .noneMatch { board[it % 3][it / 3] == EMPTY }
    }

    fun getWinner(): TicTacToeSquare {
        // Verify diagonals
        val center = board[1][1]
        if (center != EMPTY &&
                (center == board[0][0] && center == board[2][2]
                        || center == board[0][2] && center == board[2][0])) {
            return center
        }

        // Verify verticals and horizontals
        for (i in 0 until 3) {
            val pivot = board[i][i]
            if (pivot != EMPTY
                    && (pivot == board[i][0] && pivot == board[i][1] && pivot == board[i][2]
                            || pivot == board[0][i] && pivot == board[1][i] && pivot == board[2][i])) {
                return pivot
            }
        }

        return EMPTY
    }

    private fun createNew(player: TicTacToeSquare, x: Int, y: Int): TicTacToeBoard {
        val newTempBoard = mutableListOf<List<TicTacToeSquare>>()
        for (i in 0 until 3) {
            val newTempBoardLine = mutableListOf<TicTacToeSquare>()
            for (j in 0 until 3) {
                val square = if (x == i && y == j) player else board[i][j]
                newTempBoardLine.add(square)
            }
            newTempBoard.add(newTempBoardLine)
        }

        val newBoard = listOf(*newTempBoard.map { listOf(*it.toTypedArray()) }.toTypedArray())
        return TicTacToeBoard(newBoard)
    }

    override fun toString(): String {
        return "| ${board[0][0].symbol} |  ${board[1][0].symbol} |  ${board[2][0].symbol} |\n" +
                "| ${board[0][1].symbol} |  ${board[1][1].symbol} |  ${board[2][1].symbol} |\n" +
                "| ${board[0][2].symbol} |  ${board[1][2].symbol} |  ${board[2][2].symbol} |"
    }
}