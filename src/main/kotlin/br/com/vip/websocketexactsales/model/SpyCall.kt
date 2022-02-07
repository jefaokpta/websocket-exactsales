package br.com.vip.websocketexactsales.model

data class SpyCall(
    val type: MessageType = MessageType.SPY,
    val orgId: Int,
    val userId: Int,
    val callId: String,
    val callType: String,
) {
}