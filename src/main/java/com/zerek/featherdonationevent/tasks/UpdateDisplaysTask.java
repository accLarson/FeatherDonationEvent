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
        this.plugin.getLogger().info("Successfully started donor display.");
    }

    @Override
    public void run() {
        plugin.getLogger().info("Updating " + plugin.getGeneralDisplays().size() + " display(s).");
        List<String> displayedDonorsList = new ArrayList<>();

        // Iterate each donor display and set new stand skull and sign
        plugin.getGeneralDisplays().keySet().forEach(k -> {
            OfflinePlayer donor = getRandomDonor(plugin.getDonors());
            plugin.removeDonor(donor);
            displayedDonorsList.add(donor.getName());
            setStandHead(plugin.getGeneralDisplays().get(k).keySet().iterator().next(), donor);
            setSign(plugin.getGeneralDisplays().get(k).get(plugin.getGeneralDisplays().get(k).keySet().iterator().next()), donor);
        });
        plugin.getServer().broadcast(MiniMessage.miniMessage().deserialize(generalDisplayUpdated, Placeholder.unparsed("donors", String.join(" ",displayedDonorsList))));
    }

    private OfflinePlayer getRandomDonor(ArrayList<OfflinePlayer> donors) {
        return donors.get(new Random().nextInt(donors.size()));
    }

    private void setStandHead(ArmorStand stand, OfflinePlayer donor){
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwningPlayer(donor);
        skull.setItemMeta(skullMeta);
        stand.getEquipment().setHelmet(skull);
    }

    private void setSign(Sign sign, OfflinePlayer donor){
        sign.line(1, MiniMessage.miniMessage().deserialize("<white><donor>",Placeholder.unparsed("donor", donor.getName())));
        sign.line(2, MiniMessage.miniMessage().deserialize("<#ffeb8b>Donor"));
        sign.update();
    }
}