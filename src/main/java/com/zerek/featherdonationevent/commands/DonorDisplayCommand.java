package com.zerek.featherdonationevent.commands;

import com.zerek.featherdonationevent.FeatherDonationEvent;
import com.zerek.featherdonationevent.tasks.UpdateDisplaysTask;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

public class DonorDisplayCommand implements CommandExecutor {

    private final FeatherDonationEvent plugin;
    private final String noCommandPermission;

    public DonorDisplayCommand(FeatherDonationEvent plugin) {
        this.plugin = plugin;
        noCommandPermission = this.plugin.getConfig().getString("messages.no-command-permission");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(sender instanceof ConsoleCommandSender || sender.isOp()){
            if (args[0].equalsIgnoreCase("update")){
                plugin.getServer().getScheduler().runTask(plugin,new UpdateDisplaysTask(plugin));
                return true;
            }
            else return false;
        }
        else sender.sendMessage(MiniMessage.miniMessage().deserialize(noCommandPermission));
        return true;
    }
}
