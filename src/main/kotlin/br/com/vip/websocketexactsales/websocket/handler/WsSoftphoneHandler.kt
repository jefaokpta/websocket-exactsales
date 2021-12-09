package br.com.vip.websocketexactsales.websocket.handler

import br.com.vip.websocketexactsales.model.*
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import java.util.*
import kotlin.collections.HashMap

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
                    UserCentral.users[key.split("-")[0]]?.remove(key.split("-")[1])
                }
            }
            println("SESSION FOI EMBORA ID: " + session.id)
        }

    fun addSession(webSocketMessage: WebSocketMessage, webSocketSession: WebSocketSession) =
        jacksonObjectMapper().readValue(webSocketMessage.payloadAsText, ObjectNode::class.java)
            .let { jsonNode ->
                when (jsonNode["type"].asText()) {
                    MessageType.REGISTER.toString() -> {
                        WsSessionCentral.sessions["${jsonNode["orgId"]}-${jsonNode["userId"]}"] = webSocketSession
                        UserCentral.setUser(User(jsonNode["orgId"].asText(), jsonNode["userId"].asText()))
                        jacksonObjectMapper().writeValueAsString(PeerAuth())
                    }
                    MessageType.STATUS.toString() -> {
                        WsSessionCentral.sessions[jsonNode["session"].asText()].let {
                            it?.attributes?.set("status", jsonNode["status"].asText())
                        }
                        webSocketMessage.payloadAsText
                    }
                    else -> {
                        println("DADOS NAO RECONHECIDOS ${webSocketMessage.payloadAsText}")
                        webSocketMessage.payloadAsText
                    }
                }
            }
    //return webSocketMessage.payloadAsText
    //}

    fun getStatus(status: Status) = Optional.ofNullable(WsSessionCentral.sessions[status.session])
        .map { Mono.just(Status(status = it.attributes["status"].toString(), session = status.session)) }
        .orElseGet { Mono.just(Status(status = "unavailable", session = status.session)) }

    fun sendMessage(call: Call) =
        Optional.ofNullable(WsSessionCentral.sessions["${call.orgId}-${call.userId}"])
            .map { it.send(Mono.just(it.textMessage(jacksonObjectMapper().writeValueAsString(call)))) }
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }

}