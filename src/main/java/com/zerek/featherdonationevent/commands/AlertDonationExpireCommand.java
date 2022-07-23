package com.zerek.featherdonationevent.commands;

import com.zerek.featherdonationevent.FeatherDonationEvent;
import com.zerek.featherdonationevent.events.DonationExpireEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

public class AlertDonationExpireCommand implements CommandExecutor {

    //fields
    private final FeatherDonationEvent plugin;

    //constructor
    public AlertDonationExpireCommand(FeatherDonationEvent plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        //alertdonationexpire [0]{purchaserName} [1]{purchaserUuid} [2]{username} [3]{id} [4]{packageName} [5]{price}

        if (sender instanceof ConsoleCommandSender) {

            DonationExpireEvent event = new DonationExpireEvent(args[0], args[1], args[2], args[3], args[4], args[5]);
            plugin.getServer().getPluginManager().callEvent(event);
            return true;
        }
        return false;
    }
}
