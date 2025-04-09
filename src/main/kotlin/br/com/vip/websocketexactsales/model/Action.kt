package br.com.vip.websocketexactsales.model

class Action(
    val type: MessageType,
    val session: String,
    val status: String?
) {
}