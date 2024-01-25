package br.com.vip.websocketexactsales.asterisk

import br.com.vip.websocketexactsales.cache.ChannelCache
import org.asteriskjava.manager.ManagerConnection
import org.asteriskjava.manager.ManagerConnectionFactory
import org.asteriskjava.manager.ManagerEventListener
import org.asteriskjava.manager.action.GetVarAction
import org.asteriskjava.manager.action.ManagerAction
import org.asteriskjava.manager.event.DeviceStateChangeEvent
import org.asteriskjava.manager.event.DialEvent
import org.asteriskjava.manager.event.ManagerEvent
import org.asteriskjava.manager.event.NewStateEvent
import org.asteriskjava.manager.response.GetVarResponse
import org.asteriskjava.manager.response.ManagerResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 * @author Jefferson Alves Reis (jefaokpta) < jefaokpta@hotmail.com >
 * Date: 10/10/23
 */
@Component
class AmiCache(private val channelCache: ChannelCache): ManagerEventListener {

    @Value("\${asterisk.host}")
    private lateinit var asteriskHost: String

    @Value("\${asterisk.ami.port}")
    private lateinit var asteriskAmiPort: String

    @Value("\${asterisk.ami.username}")
    private lateinit var asteriskAmiUsername: String

    @Value("\${asterisk.ami.password}")
    private lateinit var asteriskAmiPassword: String

    private lateinit var ami: ManagerConnection

    fun amiInitialize(){
        ami = ManagerConnectionFactory(
            asteriskHost,
            asteriskAmiPort.trim().toInt(),
            asteriskAmiUsername,
            asteriskAmiPassword
        ).createManagerConnection()
        ami.addEventListener(this)
        ami.login()
        println("AMI CONECTADO A ${ami.version} 👍🏼")
    }

    private fun sendActionAsync(action: ManagerAction) {
        ami.sendAction(action){
            println("AMI ASYNC RESPONSE: $it")
        }
    }

    private fun getVariableAsync(channel: String, variable: String, callback: (ManagerResponse) -> Unit) {
        ami.sendAction(GetVarAction(channel, variable)){
            callback(it)
        }
    }

    override fun onManagerEvent(event: ManagerEvent) {
        //println(event)
        when(event){
            is NewStateEvent -> {
                println(event)
                if (event.channelState == 5) {
                    getVariableAsync(channelCache.removeChannel(event.channel) ?: return, "DST") {
                        val response  = it as GetVarResponse
                        println("AMI ASYNC RESPONSE: ${response.value} - CHANNEL ${event.channel}")
                    }
                }
            }
            is DialEvent -> {
                println(event)
                if (event.subEvent == "Begin") {
                    channelCache.addChannel(event.channel, event.destination)
                }
            }
            is DeviceStateChangeEvent -> println(event)

        }
        /**
        if (event is VarSetEvent) return // nova feature ok
        if (event is ExtensionStatusEvent) return
        if (event is NewExtenEvent) return
        if (event is QueueSummaryCompleteEvent) return
        if (event is QueueStatusCompleteEvent) return
        if (event is RtcpSentEvent) return
        if (event is RtcpReceivedEvent) return
        if (event is ChannelReloadEvent) return
        if (event is NewCallerIdEvent) return
        if (event is BridgeEvent) {
        //HandleEvent().handle(event as BridgeEvent?)
        return
        }
        if (event is NewAccountCodeEvent) return
        if (event is ChannelUpdateEvent) return
        if (event is JitterBufStatsEvent) return
        if (event is DialEvent) {
        //HandleEvent().handle(event as DialEvent?)
        return
        }
        if (event is MusicOnHoldEvent) return
        if (event is RenameEvent) return
        if (event is MasqueradeEvent) return
        if (event is DtmfEvent) return
        if (event is JoinEvent) return
        if (event is QueueMemberAddedEvent) return
        if (event is RegistryEvent) return
        if (event is SoftHangupRequestEvent) return
        if (event is HangupRequestEvent) return
        if (event is HoldEvent) return
        if (event is UnholdEvent) return
        if (event is AgentCompleteEvent) return
        if (event is AgentRingNoAnswerEvent) return
        if (event is DisconnectEvent) return
        if (event is FullyBootedEvent) return
        if (event is ConnectEvent) return
        if (event is MessageWaitingEvent) return
        if (event is MeetMeJoinEvent) return
        if (event is MeetMeEndEvent) return
        if (event is MeetMeLeaveEvent) return
        if (event is ConfbridgeEndEvent) return
        if (event is ConfbridgeJoinEvent) return
        if (event is ConfbridgeLeaveEvent) return

        // ASTERISK_JAVA 3.6
        if (event is SuccessfulAuthEvent) return
        if (event is ChallengeSentEvent) return
        if (event is DeviceStateChangeEvent) return
        if (event is InvalidPasswordEvent) return
        if (event is NewConnectedLineEvent) return
        if (event is BridgeEnterEvent) return
        if (event is QueueCallerLeaveEvent) return
        if (event is BridgeCreateEvent) return
        if (event is BridgeDestroyEvent) return
        if (event is BridgeLeaveEvent) return
        if (event is LocalBridgeEvent) return
        if (event is QueueCallerJoinEvent) return
        if (event is LocalOptimizationEndEvent) return
        if (event is BlindTransferEvent) return
        if (event is AttendedTransferEvent) return
        if (event is BridgeMergeEvent) return
        if (event is LocalOptimizationBeginEvent) return

        println(event)
         **/
    }
}