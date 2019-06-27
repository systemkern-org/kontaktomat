package com.systemkern.kontactomat

import com.systemkern.kontactomat.mail.GmailService
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class CommandLinesComponent(
        private val gmailService: GmailService
): CommandLineRunner {

    override fun run(vararg args: String?) {
        var optSelected: Int = -1
        print("\n")
        createMenu(listOf("Welcome auth", "Read and chose an option to continue"))
        while (optSelected != 5) {
            print("\n")
            createMenu(listOf("1. Read gmail inbox", "5. Exit"))
            optSelected = Integer.parseInt(readLine())

            if(gmailService.isAuthenticated){
                when(optSelected){
                    1 -> gmailService.getInbox()
                }
            }else{
                println("Access not allowed, please authenticate in:")
                println("http://localhost:8080")
            }
        }
        System.exit(0)
    }

    private fun createMenu(options: List<String>) =
            options.forEach {
                print(it)
                print("\n")
            }

}