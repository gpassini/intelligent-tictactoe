package com.intelligenttictactoe.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Application launcher.
 */
@SpringBootApplication
open class DemoApplication

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}
