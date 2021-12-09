package br.com.vip.websocketexactsales.controller

import br.com.vip.websocketexactsales.model.Status
import br.com.vip.websocketexactsales.model.User
import br.com.vip.websocketexactsales.model.UserCentral
import br.com.vip.websocketexactsales.model.WsSessionCentral
import br.com.vip.websocketexactsales.websocket.handler.WsSoftphoneHandler
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

/**
 * @author Jefferson Alves Reis (jefaokpta) < jefaokpta@hotmail.com >
 * Date: 2020-12-14
 */
@RestController
@RequestMapping("/v1/api/status")
class StatusController(private val wsSoftphoneHandler: WsSoftphoneHandler) {

    @GetMapping("/users/{orgId}")
    fun getUserStatus(@PathVariable("orgId") orgId: String) = Mono.justOrEmpty(UserCentral.users[orgId])
        .map { HashMap(it).values.toList() }
        .switchIfEmpty(Mono.just(listOf()))

    @PostMapping("/users")
    fun setUserStatus(@RequestBody user: User) = Mono.justOrEmpty(UserCentral.setUser(user))
        .then()

    @GetMapping("/{session}")
    fun consulta(@PathVariable session: String) = wsSoftphoneHandler.getStatus(Status(session = session))

    @GetMapping
    fun listaSessoes() = Mono.justOrEmpty(WsSessionCentral.sessions.keys.toList())
}