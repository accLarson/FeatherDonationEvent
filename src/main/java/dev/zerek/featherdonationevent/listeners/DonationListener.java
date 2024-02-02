package dev.zerek.featherdonationevent.listeners;

import dev.zerek.featherdonationevent.FeatherDonationEvent;
import dev.zerek.featherdonationevent.events.DonationEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class DonationListener implements Listener {

    private final FeatherDonationEvent plugin;

    public DonationListener(FeatherDonationEvent plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDonation(DonationEvent event){
        OfflinePlayer donor = plugin.getServer().getOfflinePlayer(UUID.fromString(event.getId()));
        if (plugin.getNewestDisplay().keySet().iterator().next().getChunk().isEntitiesLoaded()){
            setStandHead(plugin.getNewestDisplay().keySet().iterator().next(), donor);
            setSign(plugin.getNewestDisplay().get(plugin.getNewestDisplay().keySet().iterator().next()), donor);
        }
    }

    private void setStandHead(ArmorStand stand, OfflinePlayer donor){
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwningPlayer(donor);
        skull.setItemMeta(skullMeta);
        stand.getEquipment().setHelmet(skull);
    }

    private void setSign(Sign sign, OfflinePlayer donor){
        sign.line(1, MiniMessage.miniMessage().deserialize("<white><donor>", Placeholder.unparsed("donor", donor.getName())));
        sign.line(2, MiniMessage.miniMessage().deserialize("<#ffeb8b>Newest Donor"));
        sign.update();
    }
}
