package com.zerek.featherdonationevent.listeners;

import com.zerek.featherdonationevent.FeatherDonationEvent;
import com.zerek.featherdonationevent.events.DonationEvent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.UUID;

public class DonationListener implements Listener {

    private final FeatherDonationEvent plugin;

    public DonationListener(FeatherDonationEvent plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDonation(DonationEvent event){
        OfflinePlayer p = plugin.getServer().getOfflinePlayer(UUID.fromString(event.getId()));

        Location standLoc = plugin.getNewDisplay().get("stand").getBlock().getLocation();
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

        Sign sign = (Sign)plugin.getNewDisplay().get("sign").getBlock().getState();
        sign.setLine(1, ChatColor.of("#ffeb8b") + "NEWEST Donor");
        sign.setLine(2, ChatColor.of("#ffffff") + p.getName());
        sign.update();
    }
}
