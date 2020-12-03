package br.com.vip.websocketexactsales.controller

import br.com.vip.websocketexactsales.model.Call
import br.com.vip.websocketexactsales.websocket.handler.WsSoftphoneHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author Jefferson Alves Reis (jefaokpta) < jefaokpta@hotmail.com >
 * Date: 2020-12-02
 */
@RestController
@RequestMapping("/v1/api/call")
class CallController(private val wsSoftphoneHandler: WsSoftphoneHandler) {

    @PostMapping
    fun doCall(@RequestBody call: Call) = wsSoftphoneHandler.sendMessage(call)
}