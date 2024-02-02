package dev.zerek.featherdonationevent.commands;

import dev.zerek.featherdonationevent.FeatherDonationEvent;
import dev.zerek.featherdonationevent.events.SupporterRenewEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

public class AlertSupporterRenewCommand implements CommandExecutor {

    //fields
    private final FeatherDonationEvent plugin;

    //constructor
    public AlertSupporterRenewCommand(FeatherDonationEvent plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        //alertsupporterrenew [0]{purchaserName} [1]{purchaserUuid} [2]{username} [3]{id} [4]{packageName} [5]{price}

        if (sender instanceof ConsoleCommandSender) {

            SupporterRenewEvent event = new SupporterRenewEvent(args[0], args[1], args[2], args[3], args[4], args[5]);
            plugin.getServer().getPluginManager().callEvent(event);
            return true;
        }
        return false;
    }
}
