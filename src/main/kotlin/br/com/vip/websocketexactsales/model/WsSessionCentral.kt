package br.com.vip.websocketexactsales.model

import org.springframework.web.reactive.socket.WebSocketSession

class WsSessionCentral {

    companion object {
        val sessions = mutableMapOf<String, WebSocketSession>()
    }
}