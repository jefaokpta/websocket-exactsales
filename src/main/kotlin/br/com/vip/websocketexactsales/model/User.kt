package br.com.vip.websocketexactsales.model

import java.time.LocalDateTime

/**
 * @author Jefferson Alves Reis (jefaokpta) < jefaokpta@hotmail.com >
 * Date: 08/12/21
 */
class User(
    val orgId: String,
    val userId: String,
    val status: Int = 3,
    val elapsedTime: LocalDateTime? = null,
    val callId: String? = null,
    val leadId: String? = null,
) {
}