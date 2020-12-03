package br.com.vip.websocketexactsales.websocket.handler

import br.com.vip.websocketexactsales.model.Call
import br.com.vip.websocketexactsales.model.WsSessionCentral
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.*

@Service
class WsSoftphoneHandler: WebSocketHandler {

    override fun handle(session: WebSocketSession) = session.send(session.receive()
            .doFirst {
                println("SESSION ID : ${session.id} CONECTOU")
            }
            .map{add(it, session)}
            .map(session::textMessage))
            .doFinally {
                WsSessionCentral.sessions.values.remove(session)
                println("SESSION FOI EMBORA ID: " + session.id)
            }

    fun add(webSocketMessage: WebSocketMessage, webSocketSession: WebSocketSession) =
        ObjectMapper().readValue(webSocketMessage.payloadAsText, ObjectNode::class.java).let {
        WsSessionCentral.sessions["${it["userId"]}${it["orgId"]}"] = webSocketSession
        webSocketMessage.payloadAsText
    }

    fun sendMessage(call: Call) =
        Optional.ofNullable(WsSessionCentral.sessions[call.userId + call.orgId])
            .map { it.send(Mono.just(it.textMessage(jacksonObjectMapper().writeValueAsString(call)))) }
            .orElseThrow{ResponseStatusException(HttpStatus.NOT_FOUND)}

}