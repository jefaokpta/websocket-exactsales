package br.com.vip.websocketexactsales.model

import java.time.LocalDateTime

/**
 * @author Jefferson Alves Reis (jefaokpta) < jefaokpta@hotmail.com >
 * Date: 08/12/21
 */
class User(
    val orgId: String,
    val userId: String,
    var status: Int = 3,
    var elapsedTime: LocalDateTime? = null,
    var callId: String? = null,
    var leadId: String? = null,
) {
}