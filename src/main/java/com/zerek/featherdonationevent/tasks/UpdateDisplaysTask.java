package com.zerek.featherdonationevent.tasks;

import com.zerek.featherdonationevent.FeatherDonationEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UpdateDisplaysTask implements Runnable{

    private final FeatherDonationEvent plugin;
    private final String generalDisplayUpdated;

    public UpdateDisplaysTask(FeatherDonationEvent plugin) {
        this.plugin = plugin;
        generalDisplayUpdated = this.plugin.getConfig().getString("messages.general-display-updated");
    }

    //select random donor from ArrayList
    private OfflinePlayer getRandomDonor(ArrayList<OfflinePlayer> donors) {
        return donors.get(new Random().nextInt(donors.size()));
    }

    @Override
    public void run() {
        plugin.getLogger().info("Updating display(s) | " + plugin.getGeneralDisplays().keySet().size() + " displays found in config file");
        List<String> displayedDonors = new ArrayList<>();
        plugin.getGeneralDisplays().keySet().forEach(display -> {
            OfflinePlayer donor = getRandomDonor(plugin.getDonors());
            plugin.removeDonor(donor);
            displayedDonors.add(donor.getName());
            plugin.getGeneralDisplays().get(display).get("stand").getBlock().getLocation().getChunk().load();
            setStandHead(donor, plugin.getGeneralDisplays().get(display).get("stand").getBlock().getLocation());
            setSign(donor, plugin.getGeneralDisplays().get(display).get("sign"));
        });
        plugin.getServer().broadcast(MiniMessage.miniMessage().deserialize(generalDisplayUpdated, Placeholder.unparsed("donors", String.join(" ",displayedDonors))));
    }

    private void setStandHead(OfflinePlayer p, Location standLoc){

        standLoc.getChunk().load();
        List<ArmorStand> standList = (List<ArmorStand>) standLoc.toCenterLocation().getNearbyEntitiesByType(ArmorStand.class, 0.5,1.0,0.5);
        if (standList.size() > 0) {
            ArmorStand stand = standList.get(0);
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
            SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
            skullMeta.setOwningPlayer(p);
            skull.setItemMeta(skullMeta);
            stand.getEquipment().setHelmet(skull);
        } else plugin.getLogger().warning("no armor stand found at: " + standLoc.getBlockX() + " " + standLoc.getBlockY() + " " + standLoc.getBlockZ());
    }

    private void setSign(OfflinePlayer p, Location signLoc){
        signLoc.getChunk().load();
        Sign sign = (Sign)signLoc.getBlock().getState();
        sign.setLine(1, ChatColor.of("#ffeb8b") + "Donor");
        sign.setLine(2, ChatColor.of("#ffffff") + p.getName());
        sign.update();
    }
}