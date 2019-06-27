package com.systemkern.kontactomat.auth

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Controller
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
import com.systemkern.kontactomat.mail.GmailService
import java.io.FileInputStream


@Controller
class authController(
        private val gmailService: GmailService
){

    private val logger = Logger.getLogger(this::class.java.name)

    @GetMapping("/login-success")
    fun showLoginSucessPage(authentication: OAuth2AuthenticationToken): String {
        val projectPath = System.getProperty("user.dir")
        logger.log(Level.INFO, projectPath)
        val jsonFactory = JacksonFactory.getDefaultInstance()
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
        val clientSecrets = GoogleClientSecrets.load(
                jsonFactory,
                InputStreamReader(
                        FileInputStream("$projectPath/credentials.json")
                ))
        val flow = GoogleAuthorizationCodeFlow.Builder(
                httpTransport,
                jsonFactory,
                clientSecrets,
                setOf(GmailScopes.MAIL_GOOGLE_COM)
        ).build()

        val receiver = LocalServerReceiver.Builder().setPort(8888).build()
        val credentials = AuthorizationCodeInstalledApp(flow, receiver).authorize(authentication.name)

        gmailService.userId = authentication.name
        gmailService.gmail = Gmail.Builder(httpTransport, jsonFactory, credentials)
                .setApplicationName("KontaktoMat")
                .build()
        gmailService.isAuthenticated = true

        return "loginSuccessV"
    }

    @GetMapping("/login-failure")
    fun showLoginFailurePage(authentication: OAuth2AuthenticationToken) = "loginFailureV"


}
