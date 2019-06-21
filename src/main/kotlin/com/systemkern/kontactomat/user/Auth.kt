package com.systemkern.kontactomat.user

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import java.util.logging.Level
import java.util.logging.Logger

@Controller
class AuthController{

    private val logger = Logger.getLogger(this::class.java.name)

    @GetMapping("/loginSuccess")
    fun loginSucessPageShow(authentication: OAuth2AuthenticationToken): String {
        logger.log(Level.INFO, authentication.authorizedClientRegistrationId)
        logger.log(Level.INFO, authentication.name )
        logger.log(Level.INFO, authentication.authorities.toString())
        logger.log(Level.INFO, authentication.credentials.toString())

        return "loginSuccessV"
    }

    @GetMapping("/loginFailure")
    fun loginFailurePageShow(authentication: OAuth2AuthenticationToken): String {
        logger.log(Level.INFO, authentication.authorizedClientRegistrationId)
        logger.log(Level.INFO, authentication.name )
        logger.log(Level.INFO, authentication.authorities.toString())
        logger.log(Level.INFO, authentication.credentials.toString())

        return "loginFailureV"
    }
}
