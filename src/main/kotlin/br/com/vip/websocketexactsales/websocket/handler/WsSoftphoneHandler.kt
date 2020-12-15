package br.com.vip.websocketexactsales.websocket.handler

import br.com.vip.websocketexactsales.model.Call
import br.com.vip.websocketexactsales.model.MessageType
import br.com.vip.websocketexactsales.model.Status
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
import java.lang.Exception
import java.util.*

@Service
class WsSoftphoneHandler: WebSocketHandler {

    override fun handle(session: WebSocketSession) = session.send(session.receive()
            .doFirst {
                println("SESSION ID : ${session.id} CONECTOU")
            }
            .map{addSession(it, session)}
            .map(session::textMessage))
            .doFinally {
                WsSessionCentral.sessions.values.remove(session)
                println("SESSION FOI EMBORA ID: " + session.id)
            }

    fun addSession(webSocketMessage: WebSocketMessage, webSocketSession: WebSocketSession): String {
        jacksonObjectMapper().readValue(webSocketMessage.payloadAsText, ObjectNode::class.java).let { jsonNode ->
                when (jsonNode["type"].asText()){
                    MessageType.REGISTER.toString() -> WsSessionCentral.sessions["${jsonNode["userId"]}${jsonNode["orgId"]}"] = webSocketSession
                    MessageType.STATUS.toString() -> WsSessionCentral.sessions[jsonNode["session"].asText()].let {
                        it?.attributes?.set("status", jsonNode["status"].asText())}
                    else -> println("DADOS NAO RECONHECIDOS ${webSocketMessage.payloadAsText}")
                }
        }
        return webSocketMessage.payloadAsText
    }

    fun getStatus(status: Status) = Optional.ofNullable(WsSessionCentral.sessions[status.session])
            .map{Mono.just(Status(status = it.attributes["status"].toString(), session = status.session))}
            .orElseGet{Mono.just(Status(status = "unavailable", session = status.session))}

    fun sendMessage(call: Call) =
        Optional.ofNullable(WsSessionCentral.sessions[call.userId.toString() + call.orgId.toString()])
            .map { it.send(Mono.just(it.textMessage(jacksonObjectMapper().writeValueAsString(call)))) }
            .orElseThrow{ResponseStatusException(HttpStatus.NOT_FOUND)}

}