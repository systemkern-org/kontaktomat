package com.systemkern.kontactomat.user

import org.springframework.boot.CommandLineRunner
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import java.util.logging.Level
import java.util.logging.Logger

@Controller
class mainController(
        private val gmailService: GmailService,
        private var token: OAuth2AuthenticationToken ?= null
): CommandLineRunner {

    override fun run(vararg args: String?) {
        var optSelected: Int = -1
        print("\n")
        createMenu(listOf("Welcome user", "Read and chose an option to continue"))
        while (optSelected != 5){
            print("\n")
            createMenu(listOf("1. Read gmail inbox", "5. Exit"))
            optSelected = Integer.parseInt(readLine())

            if(optSelected == 1){
                token?.let { gmailService.getInbox() }
            }
        }
        System.exit(0)
    }

    private val logger = Logger.getLogger(this::class.java.name)

    @GetMapping("/loginSuccess")
    fun showLoginSucessPage(authentication: OAuth2AuthenticationToken): String {
        token = authentication
        logger.log(Level.INFO, token?.name)

        return "loginSuccessV"
    }

    @GetMapping("/loginFailure")
    fun showLoginFailurePage(authentication: OAuth2AuthenticationToken)
            = "loginFailureV"

    private fun createMenu(options: List<String>) =
            options.forEach {
                print(it)
                print("\n")
            }
}



@Service class GmailService{
    internal fun getInbox(){

    }
}