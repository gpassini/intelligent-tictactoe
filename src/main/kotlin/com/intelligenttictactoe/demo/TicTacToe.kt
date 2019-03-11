package com.intelligenttictactoe.demo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@ShellComponent
class TicTacToe @Autowired constructor(
        private val ticTacToeService: TicTacToeService
) {
    @ShellMethod(value = "Choose a position to play (0 to 8).", key = ["play", "p"])
    fun play(@Min(0) @Max(8) position: Int): String {
        ticTacToeService.play(position)
        return ticTacToeService.print()
    }

    @ShellMethod(value = "Play randomly.", key = ["random", "rand", "r"])
    fun random(): String {
        ticTacToeService.playRandom()
        return ticTacToeService.print()
    }

    @ShellMethod(value = "Starts a new game", key = ["start", "s"])
    fun start(): String {
        ticTacToeService.resetGame()
        return ticTacToeService.print()
    }
}