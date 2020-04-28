package net.minecord.cordexproxy.listener

import net.minecord.cordexproxy.CordexProxy
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.ChatEvent
import net.md_5.bungee.event.EventHandler

class SecurityListener(cordexProxy: CordexProxy) : BaseListener(cordexProxy) {
    @EventHandler
    fun onChat(e: ChatEvent) {
        val command = e.message
        if (command.startsWith("/l ") || command.startsWith("/login ") || command.startsWith("/reg ") || command.startsWith("/register "))
            return

        val cordPlayer = cordexProxy.playerController.getPlayer(e.sender as ProxiedPlayer)
        if (!command.startsWith("/")) {
            val isMuted = cordexProxy.banController.isMuted(cordPlayer)
            if (isMuted) {
                cordPlayer.sendMessage("banlist", cordPlayer.translateMessage("muteChatTry"))
                e.isCancelled = true
                return
            }
        }

        var isLogged = cordPlayer.data.isLogged
        if (!isLogged) {
            isLogged = cordexProxy.databaseController.isLogged(cordPlayer.data.id)
            cordPlayer.data.isLogged = isLogged
        }

        if (!isLogged) {
            cordPlayer.sendMessage("auth", cordPlayer.translateMessage("loginCommandDenied"))
            e.isCancelled = true
        }
    }
}
