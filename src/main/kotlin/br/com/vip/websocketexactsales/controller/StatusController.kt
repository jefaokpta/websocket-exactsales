package br.com.vip.websocketexactsales.controller

import br.com.vip.websocketexactsales.model.Status
import br.com.vip.websocketexactsales.model.WsSessionCentral
import br.com.vip.websocketexactsales.websocket.handler.WsSoftphoneHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

/**
 * @author Jefferson Alves Reis (jefaokpta) < jefaokpta@hotmail.com >
 * Date: 2020-12-14
 */
@RestController
@RequestMapping("/v1/api/status")
class StatusController(private val wsSoftphoneHandler: WsSoftphoneHandler) {

    @GetMapping("/{session}")
    fun consulta(@PathVariable session: String) = wsSoftphoneHandler.getStatus(Status(session = session))

    @GetMapping
    fun listaSessoes() = Mono.justOrEmpty(WsSessionCentral.sessions.keys.toList())
}