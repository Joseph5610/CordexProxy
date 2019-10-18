package net.minecord.cordexproxy.command

import net.md_5.bungee.api.ProxyServer
import net.minecord.cordexproxy.CordexProxy
import net.minecord.cordexproxy.model.controller.player.CordPlayer
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.connection.ProxiedPlayer

class WarnCommand(cordexProxy: CordexProxy, name: String, permission: String, vararg aliases: String) : BaseCommand(cordexProxy, name, permission, *aliases) {
    override fun execute(commandSender: CommandSender, args: Array<String>) {
        var admin: CordPlayer? = null
        if (commandSender is ProxiedPlayer) {
            if (!commandSender.hasPermission("cordex.warn"))
                return
            admin = cordexProxy.playerController.getPlayer(commandSender)
        }

        if (args.size < 2) {
            admin?.sendMessage("banlist", "%prefix% Example warn: &e/warn Nick Swearing &7more at &b/cordex ban")
            return
        }

        val proxiedTarget = ProxyServer.getInstance().getPlayer(args[0])
        if (proxiedTarget == null || proxiedTarget.isConnected) {
            admin?.sendMessage("banlist", "%prefix% Player not found, manual is at &b/cordex ban")
            return
        }

        val target = cordexProxy.playerController.getPlayer(proxiedTarget)

        var reason = StringBuilder()
        for (i in 1 until args.size)
            reason.append(args[i]).append(" ")

        reason = StringBuilder(reason.substring(0, reason.length - 1))

        cordexProxy.banController.warnPlayer(target, reason.toString())
    }

    override fun onTabComplete(sender: CommandSender?, args: Array<out String>?): MutableIterable<String> {
        val list = mutableListOf<String>()

        if (args == null || args.size == 1) {
            for (player in ProxyServer.getInstance().players) {
                list.add(player.name)
            }
        } else if (args.size == 2) {
            list.add("Report abusing")
            list.add("Insulting")
            list.add("Swearing")
            list.add("Advertisement")
            list.add("Spam")
            list.add("Stupidity")
        }

        return list
    }
}
