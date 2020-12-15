package br.com.vip.websocketexactsales.model

/**
 * @author Jefferson Alves Reis (jefaokpta) < jefaokpta@hotmail.com >
 * Date: 2020-12-14
 */
class Status(
    val type: MessageType = MessageType.STATUS,
    val status: String = "",
    val session: String
) {
}