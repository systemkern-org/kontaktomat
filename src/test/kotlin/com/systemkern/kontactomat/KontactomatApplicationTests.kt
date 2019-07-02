package com.systemkern.kontactomat

import com.systemkern.kontactomat.auth.AuthService
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.assertj.core.api.Assertions.assertThat
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@ComponentScan(basePackages = ["com.systemkern.kontactomat"])
@ContextConfiguration(classes = [KontactomatApplicationTests::class, ConfigFileApplicationContextInitializer::class])
@TestPropertySource(locations = ["classpath:application.properties"])
class KontactomatApplicationTests {

	@Autowired
	lateinit var service: AuthService

	@Value("\${server.port}") lateinit var serverPort: String

	@Test
	fun `Check if the authentication url is generated properly`() {
		val url = service.authorize()

		assertThat(url).contains("https://accounts.google.com/o/oauth2/auth")
		assertThat(url).contains("client_id")
		assertThat(url).contains("redirect_uri")
		assertThat(url).contains("http://localhost:$serverPort/login-success")

		assertThat(url).contains("https://mail.google.com/")
		assertThat(url).contains("https://www.googleapis.com/auth/userinfo.profile")
		assertThat(url).contains("https://www.googleapis.com/auth/userinfo.email")

	}

}

