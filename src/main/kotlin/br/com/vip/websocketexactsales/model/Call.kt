package br.com.vip.websocketexactsales.model

/**
 * @author Jefferson Alves Reis (jefaokpta) < jefaokpta@hotmail.com >
 * Date: 2020-12-02
 */
data class Call(
    val type: MessageType,
    val tel: String = "",
    val userId: String = "",
    val orgId: String = ""
)