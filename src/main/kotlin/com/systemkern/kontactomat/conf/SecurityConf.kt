package com.systemkern.kontactomat.conf

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import javax.servlet.ServletException
import java.io.IOException
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest
import org.springframework.security.web.DefaultRedirectStrategy
import org.springframework.security.web.RedirectStrategy
import org.springframework.security.web.authentication.AuthenticationFailureHandler


@Configuration
class WebSecurityConfiguration : WebSecurityConfigurerAdapter() {
    private val redirectStrategy = DefaultRedirectStrategy()

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                .successHandler(object : AuthenticationSuccessHandler {
                    @Throws(IOException::class, ServletException::class)
                    override fun onAuthenticationSuccess(
                            request: HttpServletRequest,
                            response: HttpServletResponse,
                            authentication: Authentication
                    ) {
                        redirectStrategy.sendRedirect(request, response, "/login-success")
                    }
                })
                .failureHandler(object: AuthenticationFailureHandler {
                    override fun onAuthenticationFailure(
                            request: HttpServletRequest?,
                            response: HttpServletResponse?,
                            exception: AuthenticationException?
                    ) {
                        redirectStrategy.sendRedirect(request, response, "/login-failure")
                    }
                })
    }
}