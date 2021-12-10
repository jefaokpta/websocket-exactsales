package br.com.vip.websocketexactsales.controller

import br.com.vip.websocketexactsales.model.Call
import br.com.vip.websocketexactsales.websocket.handler.WsSoftphoneHandler
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

/**
 * @author Jefferson Alves Reis (jefaokpta) < jefaokpta@hotmail.com >
 * Date: 2020-12-02
 */
@RestController
@RequestMapping("/v1/api/call")
class CallController(private val wsSoftphoneHandler: WsSoftphoneHandler) {

    @GetMapping
    fun testSSL() = Mono.just("SSL OK")
        .doFirst { println("RECEBIDO GET") }

    @PostMapping
    fun doCall(@RequestBody call: Call) = wsSoftphoneHandler.sendMessage(call)
        .doFirst { println("RECEBIDO POST $call") }

}