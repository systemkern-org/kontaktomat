package com.systemkern.kontactomat.auth

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.auth.oauth2.TokenResponse
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.gmail.Gmail
import com.systemkern.kontactomat.mail.GmailService
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.view.RedirectView


@Controller
class authController(
        private val gmailService: GmailService,
        private val authService: AuthService
) {
    private val logger = LogFactory.getLog(this::class.java)

    @GetMapping("/login")
    fun process() = RedirectView(authService.authorize())

    @GetMapping("/login-success", params = ["code"])
    fun showLoginSucessPage(@RequestParam(value = "code") code: String): String {
        logger.info("Setting Gmail client")
        gmailService.gmail = gmailService.createClient(authService.buildCredentials(code))
        gmailService.userId = getClient(authService.tokenId)
        authService.isUserAuthenticated = true
        logger.info("Gmail client was set successfully")

        return "loginSuccessV"
    }

}

@Service
class AuthService {
    var isUserAuthenticated: Boolean = false

    private val logger = LogFactory.getLog(this::class.java)

    @Value("\${google.client.client-id}")
    private lateinit var clientId: String

    @Value("\${google.client.client-secret}")
    private lateinit var clientSecret: String

    @Value("\${google.client.redirectUri}")
    private lateinit var redirectURI: String

    @Value("\${google.client.scopes}")
    private lateinit var scopes: Array<String>

    internal var flow: GoogleAuthorizationCodeFlow? = null

    internal lateinit var tokenId: String

    @Throws(Exception::class)
    internal fun authorize(): String {
        val authorizationUrl: AuthorizationCodeRequestUrl
        if (flow == null) {
            val web = GoogleClientSecrets.Details()
            web.clientId = clientId
            web.clientSecret = clientSecret
            val clientSecrets = GoogleClientSecrets().setWeb(web)
            val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
            flow = GoogleAuthorizationCodeFlow.Builder(
                    httpTransport,
                    JacksonFactory
                            .getDefaultInstance(),
                    clientSecrets,
                    scopes.asList())
                    .build()
        }
        authorizationUrl = flow!!.newAuthorizationUrl().setRedirectUri(redirectURI)
        logger.info("authorizationUrl: $authorizationUrl")
        return authorizationUrl.build()
    }

    internal fun buildCredentials(code: String): Credential? {
        val response = flow?.newTokenRequest(code)?.setRedirectUri(redirectURI)?.execute()
        response as GoogleTokenResponse
        logger.info("This is your id: ${response.idToken}")
        logger.info("This is your access token: ${response.accessToken}")

        tokenId = response.idToken
        return flow?.createAndStoreCredential(
                response,
                tokenId
        )
    }

    internal fun buildCredentialsFromToken(accessToken: String) = GoogleCredential().setAccessToken(accessToken)
}

fun getClient(token: String) =
        GoogleIdToken
                .parse(
                        JacksonFactory.getDefaultInstance(),
                        token
                )
                .payload["sub"]
                .toString()
