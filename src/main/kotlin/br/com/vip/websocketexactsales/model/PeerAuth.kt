package br.com.vip.websocketexactsales.model

/**
 * @author Jefferson Alves Reis (jefaokpta) < jefaokpta@hotmail.com >
 * Date: 2020-12-17
 */
class PeerAuth(
    val type: MessageType = MessageType.PEER,
    val peer: Int = 1001002,
    val password: String = "jefao123"
) {
}