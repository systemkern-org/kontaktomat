package com.systemkern.kontactomat.user

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import org.springframework.boot.CommandLineRunner
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import java.util.logging.Level
import java.util.logging.Logger
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.services.gmail.GmailScopes
import java.io.InputStreamReader
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.gmail.Gmail
import com.google.api.services.gmail.model.ListLabelsResponse
import java.io.FileInputStream
import java.nio.file.Files


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
        token?.principal?.attributes?.forEach {
            println("${it.key} / ${it.value}")
        }
        val projectPath = System.getProperty("user.dir")
        logger.log(Level.INFO, projectPath)
        val JSON_FACTORY = JacksonFactory.getDefaultInstance()
        val HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
        val clientSecrets = GoogleClientSecrets.load(
                JSON_FACTORY,
                InputStreamReader(
                        FileInputStream("$projectPath/credentials.json")
                        //this::class.java.getResourceAsStream("$projectPath/credentials.json")
                ))
        val flow = GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, setOf(GmailScopes.MAIL_GOOGLE_COM)).build()
        val receiver = LocalServerReceiver.Builder().setPort(8888).build()
        val credentials = AuthorizationCodeInstalledApp(flow, receiver).authorize(token?.name)

        val service = Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials)
                .setApplicationName("KontaktoMat")
                .build()

        //Added code for test purposes
        val listResponse = service.users().labels().list(token?.name).execute()
        listResponse.labels.forEach{
            println(it)
        }

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