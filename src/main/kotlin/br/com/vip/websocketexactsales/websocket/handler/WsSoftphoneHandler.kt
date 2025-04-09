package br.com.vip.websocketexactsales.websocket.handler

import br.com.vip.websocketexactsales.model.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.util.*

@Service
class WsSoftphoneHandler : WebSocketHandler {

    override fun handle(session: WebSocketSession) = session.send(session.receive()
        .doFirst {
            println("SESSION ID : ${session.id} CONECTOU")
        }
        .map { addSession(it, session) }
        .map(session::textMessage))
        .doFinally {
            val sessionsCopy = HashMap(WsSessionCentral.sessions)
            sessionsCopy.forEach { (key, sessionStored) ->
                if (session.id == sessionStored.id) {
                    WsSessionCentral.sessions.remove(key)
                    UserCentral.removeUser(key)
                }
            }
            println("SESSION FOI EMBORA ID: " + session.id)
        }

    private fun addSession(webSocketMessage: WebSocketMessage, webSocketSession: WebSocketSession): String {
        val action = jacksonObjectMapper().readValue(webSocketMessage.payloadAsText, Action::class.java)
        return when (action.type) {
            MessageType.REGISTER -> {
                WsSessionCentral.sessions[action.session] = webSocketSession
                UserCentral.setUser(User(action.session.split("-")[0], action.session.split("-")[1]))
                jacksonObjectMapper().writeValueAsString(PeerAuth())
            }
            MessageType.STATUS -> {
                val session = action.session
                WsSessionCentral.sessions[session].let {
                    it?.attributes?.set("status", action.status)
                    handleUserStatus(action)
                }
                webSocketMessage.payloadAsText
            }
            else -> {
                println("DADOS NAO RECONHECIDOS ${webSocketMessage.payloadAsText}")
                webSocketMessage.payloadAsText
            }
        }
    }


    private fun handleUserStatus(action: Action) {
        when (action.status) {
            "talking" -> {
                UserCentral.getUser(action.session)?.let { user ->
                    user.status = 2
                }
            }
            "available" -> {
                UserCentral.getUser(action.session)?.let { user ->
                    user.status = 3
                    user.leadId = null
                    user.callId = null
                    user.elapsedTime = null
                }
            }
        }
    }

    fun getStatus(status: Status) = Optional.ofNullable(WsSessionCentral.sessions[status.session])
        .map { Mono.just(Status(status = it.attributes["status"].toString(), session = status.session)) }
        .orElseGet { Mono.just(Status(status = "unavailable", session = status.session)) }

    fun sendMessage(call: Call) = Optional.ofNullable(WsSessionCentral.sessions["${call.orgId}-${call.userId}"])
        .map { session ->
            session.send(Mono.just(session.textMessage(jacksonObjectMapper().writeValueAsString(call)))
                .doOnNext {
                    Optional.ofNullable(UserCentral.getUser(call.orgId.toString(), call.userId.toString()))
                        .map { user ->
                            user.status = 1
                            user.leadId = call.leadId.toString()
                            user.callId = call.record
                            user.elapsedTime = LocalDateTime.now()
                        }
                }
            )
        }
        .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }

    fun sendMessageSpyCall(spyCall: SpyCall) = Optional.ofNullable(WsSessionCentral.sessions["${spyCall.orgId}-${spyCall.userId}"])
        .map { session ->
            session.send(Mono.just(session.textMessage(jacksonObjectMapper().writeValueAsString(spyCall))))
        }
        .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Sessao nao encontrada.") }

}