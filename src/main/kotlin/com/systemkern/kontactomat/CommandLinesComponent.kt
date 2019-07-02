package com.systemkern.kontactomat

import com.systemkern.kontactomat.auth.AuthService
import com.systemkern.kontactomat.auth.getClient
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
        listOf("Welcome auth", "Read and chose an option to continue")
                .createMenu()
        listOf("1. Login", "2. Provide token access and id")
                .createMenu()

        optSelected = Integer.parseInt(readLine())

        when(optSelected){
            1 -> println("Click in: http://localhost:8080/login")
            2 -> {
                println("Token id: ")
                val tokenId = readLine().toString().trim()

                println("Access token:")
                val accessToken = readLine().toString().trim()
                gmailService.gmail = gmailService.createClient(authService.buildCredentialsFromToken(accessToken))
                gmailService.userId = getClient(tokenId)
                authService.isUserAuthenticated = true
            }
        }

        while (optSelected != 5) {
            print("\n")
            listOf("1. Read gmail inbox", "2. Get sent", "3. Get messages by sender", "5. Exit")
                    .createMenu()
            optSelected = Integer.parseInt(readLine())

            if(authService.isUserAuthenticated){
                when(optSelected){
                    1 -> gmailService.getInbox()
                    2 -> gmailService.getSent()
                    3 -> {
                        listOf("Type email to search for").createMenu()
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

}

fun List<String>.createMenu() = this.forEach {
        print(it)
        print("\n")
    }