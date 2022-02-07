package br.com.vip.websocketexactsales.controller

import br.com.vip.websocketexactsales.model.SpyCall
import br.com.vip.websocketexactsales.websocket.handler.WsSoftphoneHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/api/spycall")
class SpyCallController(private val wsSoftphoneHandler: WsSoftphoneHandler) {

    @PostMapping
    fun doSpyCall(spyCall: SpyCall) = wsSoftphoneHandler.sendMessageSpyCall(spyCall)
        .doFirst { println("RECEBIDO POST $spyCall") }
}