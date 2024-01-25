package br.com.vip.websocketexactsales.cache

import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class ChannelCache {

    private val channels = ConcurrentHashMap<String, String>()

    @Synchronized
    fun addChannel(srcChannel: String, dstChannel: String) {
        channels[dstChannel] = srcChannel
    }

    @Synchronized
    fun removeChannel(channel: String): String? {
        println("REMOVENDO CANAL $channel")
        return channels.remove(channel)
    }

}