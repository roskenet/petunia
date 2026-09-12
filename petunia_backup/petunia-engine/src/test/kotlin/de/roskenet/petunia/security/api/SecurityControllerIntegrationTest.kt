package de.roskenet.petunia.security.api

import de.roskenet.petunia.dto.CreateSecurityRequest
import de.roskenet.petunia.dto.SecurityDto
import de.roskenet.petunia.dto.UpdateSecurityRequest
import de.roskenet.petunia.security.repository.SecurityRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.resttestclient.TestRestTemplate
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class SecurityControllerIntegrationTest {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var securityRepository: SecurityRepository

    @BeforeEach
    fun setUp() {
        securityRepository.deleteAll()
    }

    @Test
    fun `securities CRUD flow works end-to-end`() {
        val createResponse = restTemplate.postForEntity(
            "/api/securities",
            CreateSecurityRequest(symbol = "BAY", name = "Bayer AG"),
            SecurityDto::class.java
        )
        assertEquals(HttpStatus.CREATED, createResponse.statusCode)
        assertNotNull(createResponse.body)
        assertEquals("BAY", createResponse.body?.symbol)
        assertEquals("Bayer AG", createResponse.body?.name)

        val getResponse = restTemplate.getForEntity("/api/securities/BAY", SecurityDto::class.java)
        assertEquals(HttpStatus.OK, getResponse.statusCode)
        assertEquals("BAY", getResponse.body?.symbol)
        assertEquals("Bayer AG", getResponse.body?.name)

        val listType = object : ParameterizedTypeReference<List<SecurityDto>>() {}
        val listResponse = restTemplate.exchange(
            "/api/securities",
            HttpMethod.GET,
            null,
            listType
        )
        assertEquals(HttpStatus.OK, listResponse.statusCode)
        assertTrue(listResponse.body?.any { it.symbol == "BAY" && it.name == "Bayer AG" } == true)

        val updateResponse = restTemplate.exchange(
            "/api/securities/BAY",
            HttpMethod.PUT,
            HttpEntity(UpdateSecurityRequest(name = "Bayer Updated")),
            SecurityDto::class.java
        )
        assertEquals(HttpStatus.OK, updateResponse.statusCode)
        assertEquals("BAY", updateResponse.body?.symbol)
        assertEquals("Bayer Updated", updateResponse.body?.name)

        val deleteResponse = restTemplate.exchange(
            "/api/securities/BAY",
            HttpMethod.DELETE,
            null,
            Void::class.java
        )
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.statusCode)

        val listAfterDeleteResponse = restTemplate.exchange(
            "/api/securities",
            HttpMethod.GET,
            null,
            listType
        )
        assertEquals(HttpStatus.OK, listAfterDeleteResponse.statusCode)
        assertFalse(listAfterDeleteResponse.body?.any { it.symbol == "BAY" } == true)
        assertFalse(securityRepository.existsById("BAY"))
    }
}

