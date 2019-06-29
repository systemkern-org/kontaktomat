package com.systemkern.kontactomat

import com.systemkern.kontactomat.auth.AuthService
import com.systemkern.kontactomat.mail.GmailService
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class CommandLinesComponent(
        private val gmailService: GmailService,
        private val authService: AuthService
): CommandLineRunner {

    override fun run(vararg args: String?) {
        var optSelected: Int
        print("\n")
        createMenu(listOf("Welcome auth", "Read and chose an option to continue"))
        createMenu(listOf("1. Login", "2. Provide token access and id"))
        optSelected = Integer.parseInt(readLine())

        when(optSelected){
            1 -> println("Click in: http://localhost:8080/login")
            2 -> {
                println("Token id: ")
                readLine()

                println("Access token")
                readLine()
            }
        }

        while (optSelected != 5) {
            print("\n")
            createMenu(listOf("1. Read gmail inbox", "2. Get sent", "3. Get messages by sender", "5. Exit"))
            optSelected = Integer.parseInt(readLine())

            if(authService.isUserAuthenticated){
                when(optSelected){
                    1 -> gmailService.getInbox()
                    2 -> gmailService.getSent()
                    3 -> {
                        createMenu(listOf("Type email to search for"))
                        gmailService.getFrom(readLine()?: "")
                    }
                }
            }else{
                println("Access not allowed, please authenticate in:")
                println("http://localhost:8080/login")
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