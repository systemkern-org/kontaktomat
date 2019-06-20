package com.systemkern.kontactomat

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KontactomatApplication: CommandLineRunner {
	override fun run(vararg args: String?) {
		var optSelected: Int = -1
		print("\n")
		createMenu(listOf("Welcome user", "Read and chose an option to continue"))
		while (optSelected != 5){
			print("\n")
			createMenu(listOf("1. Configure gmail id", "2. Continue to oAuth", "5. Exit"))
			optSelected = Integer.parseInt(readLine())

			if(optSelected == 2){

			}
		}
		System.exit(0)
	}

	fun createMenu(options: List<String>) =
		options.forEach {
			print(it)
			print("\n")
		}
}

fun main(args: Array<String>) {
	runApplication<KontactomatApplication>(*args)
}
