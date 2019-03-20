package com.intelligenttictactoe.demo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import javax.validation.constraints.Max
import javax.validation.constraints.Min

/**
 * Command line used to interact with the game.
 */
@ShellComponent
class TicTacToeShell @Autowired constructor(
        /**
         * The game's logic service.
         */
        private val ticTacToeService: TicTacToeService
) {
    /**
     * Plays at the given position.
     *
     * Positions are indexed starting from the upper left part of the board :
     *
     * 0 1 2
     *
     * 3 4 5
     *
     * 6 7 8
     */
    @ShellMethod(value = "Choose a position to play (0 to 8).", key = ["play", "p"])
    fun play(@Min(0) @Max(8) position: Int): String {
        ticTacToeService.play(position)
        return ticTacToeService.print()
    }

    /**
     * Plays at a random position.
     */
    @ShellMethod(value = "Play randomly.", key = ["random", "r"])
    fun random(): String {
        ticTacToeService.playRandom()
        return ticTacToeService.print()
    }

    /**
     * Restarts a game.
     */
    @ShellMethod(value = "Starts a new game", key = ["start", "s"])
    fun start(): String {
        ticTacToeService.resetGame()
        return ticTacToeService.print()
    }

    /**
     * Plays at a position chosen by a minimax player.
     */
    @ShellMethod(value = "Plays using the minimax strategy", key = ["minimax", "m"])
    fun minimax(): String {
        ticTacToeService.playMinimax()
        return ticTacToeService.print()
    }

    /**
     * Plays at a position chosen by a minimax player.
     */
    @ShellMethod(value = "Plays using the alphabeta pruning strategy", key = ["alphabeta", "a"])
    fun alphabeta(): String {
        ticTacToeService.playAlphabeta()
        return ticTacToeService.print()
    }

    @ShellMethod(value = "Simulate n games", key = ["simulate", "x"])
    fun simulate(@Min(1) gamesNumber: Int): String {
        return ticTacToeService.simulate(gamesNumber)
    }
}