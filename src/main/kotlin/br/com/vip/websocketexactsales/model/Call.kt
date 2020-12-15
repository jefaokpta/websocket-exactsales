package br.com.vip.websocketexactsales.model

/**
 * @author Jefferson Alves Reis (jefaokpta) < jefaokpta@hotmail.com >
 * Date: 2020-12-02
 */
data class Call(
    val type: MessageType = MessageType.CALL,
    val tel: String,
    val userId: Int,
    val orgId: Int,
    val leadId: Int,
    val callbackUrl: String
)