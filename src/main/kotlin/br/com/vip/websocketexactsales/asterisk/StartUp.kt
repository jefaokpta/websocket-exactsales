package br.com.vip.websocketexactsales.asterisk

import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class StartUp(private val amiCache: AmiCache) {
    @EventListener(ApplicationReadyEvent::class)
    @Async
    fun run() {
        amiCache.amiInitialize()



    }
}