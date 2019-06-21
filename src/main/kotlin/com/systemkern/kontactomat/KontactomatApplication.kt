package com.systemkern.kontactomat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KontactomatApplication

fun main(args: Array<String>) {
	runApplication<KontactomatApplication>(*args)
}
