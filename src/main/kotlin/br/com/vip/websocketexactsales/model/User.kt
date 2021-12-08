package br.com.vip.websocketexactsales.model

import java.time.LocalDateTime

/**
 * @author Jefferson Alves Reis (jefaokpta) < jefaokpta@hotmail.com >
 * Date: 08/12/21
 */
class User(
    val orgId: Int,
    val userId: Int,
    val callId: String = "",
    val leadId: String = "",
    val status: Int = 3,
    val elapsedTime: LocalDateTime = LocalDateTime.now(),
    val callTime: Int = 0,
) {
}