package br.com.vip.websocketexactsales.model

class Action(
    val type: MessageType,
    val orgId: String?,
    val userId: String?,
    val status: String?,
    val session: String?
) {
}