package br.com.vip.websocketexactsales.controller

import br.com.vip.websocketexactsales.model.User
import br.com.vip.websocketexactsales.model.UserCentral
import br.com.vip.websocketexactsales.model.WsSessionCentral
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Description
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@SpringBootTest
class StatusControllerTest(
    @Autowired
    private val statusController: StatusController,
) {

    @Mock
    private lateinit var webSocketSession: WebSocketSession

    @Mock
    private lateinit var webSocketMessage: WebSocketMessage

    @BeforeEach
    fun setup() {
        // Common setup for tests
        `when`(webSocketSession.id).thenReturn("test-session-id")
        `when`(webSocketSession.receive()).thenReturn(Flux.just(webSocketMessage))
        `when`(webSocketSession.send(any())).thenReturn(Mono.empty())
    }

    @Test
    fun listaSessoesTest() {
        val result = statusController.listaSessoes()
        val lista = result.block()
        println("Resultado: $lista")

        assertNotNull(lista, "O resultado deve ser diferente de nulo")
        assertTrue(lista is List<String>, "resultado deve ser uma lista")
        WsSessionCentral.sessions["123-123456"] = webSocketSession

        val response2 = statusController.listaSessoes()
        val lista2 = response2.block()
        println("Resultado2: $lista2")
        assertTrue(lista2 is List<String>, "resultado deve ser uma lista")
        assertTrue(lista2!!.all { it.contains("-") }, "todas as strings na lista devem conter o '-'")
    }

    @Test
    @Description("A Exact consome este User e estes attr sao obrigatorios")
    fun getUserStatusTest() {
        val mockUser = User(
            orgId = "321",
            userId = "123456",
            status = 2, // 1 = discando, 2 = falando, 3 = disponivel
            elapsedTime = LocalDateTime.now(),
            callId = "789",
            leadId = "101"
        )
        UserCentral.setUser(mockUser)

        val result = statusController.getUserStatus("321")
        val users = result.block()

        assertNotNull(users, "A lista de usuários não deve ser nula")
        assertTrue(users!!.isNotEmpty(), "A lista de usuários não deve estar vazia")
        val returnedUser = users[0]
        assertNotNull(returnedUser, "O usuário retornado não deve ser nulo")
        assertTrue(returnedUser.orgId == "321", "O orgId obrigatorio deve ser 321")
        assertTrue(returnedUser.userId == "123456", "O userId obrigatorio deve ser 123456")
        assertTrue(returnedUser.status == 2, "O status obrigatorio deve ser 2")
        assertNotNull(returnedUser.elapsedTime, "elapsedTime obrigatorio")
        assertTrue(returnedUser.callId == "789", "O callId obrigatorio deve ser 789")
        assertTrue(returnedUser.leadId == "101", "O leadId obrigatorio deve ser 101")
    }

}