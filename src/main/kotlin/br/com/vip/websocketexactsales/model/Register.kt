package br.com.vip.websocketexactsales.model

class Register(
    val type: MessageType,
    val orgId: String,
    val userId: String,
    val status: String?
) {
}