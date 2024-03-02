package dev.zerek.featherdonationevent.listeners;

import dev.zerek.featherdonationevent.FeatherDonationEvent;
import dev.zerek.featherdonationevent.events.DonationEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class DonationListener implements Listener {

    private final FeatherDonationEvent plugin;

    public DonationListener(FeatherDonationEvent plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDonation(DonationEvent event){
        OfflinePlayer donor = plugin.getServer().getOfflinePlayer(UUID.fromString(event.getId()));
        if (plugin.getArmorStandNew().getChunk().isEntitiesLoaded()){
            setDisplay(plugin.getArmorStandNew(),plugin.getSignNew(),donor);
        }
    }

    private void setDisplay(ArmorStand armorStand, Sign sign, OfflinePlayer donor) {
        String donorName = donor.getName();
//        ItemStack skullItem = new ItemStack(Material.PLAYER_HEAD, 1);
//        SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();
//        skullMeta.setOwningPlayer(donor);
//        skullItem.setItemMeta(skullMeta);
        Block headBlock = armorStand.getLocation().add(0.0,2.0,0.0).getBlock();
        headBlock.setType(Material.PLAYER_HEAD);
        Skull skull = (Skull) headBlock.getState();
        skull.setOwningPlayer(donor);
        ((Rotatable) skull.getBlockData()).setRotation(((Directional) sign.getBlockData()).getFacing());
        skull.update();
//        armorStand.getEquipment().clear();
//        armorStand.getEquipment().setHelmet(skull.getDrops().iterator().next());
        sign.getSide(Side.FRONT).line(0, Component.text(""));
        sign.getSide(Side.FRONT).line(1, Component.text(donorName).color(TextColor.fromHexString("#FFFFFF")));
        sign.getSide(Side.FRONT).line(2, Component.text("Newest Donor").color(TextColor.fromHexString("#FFEB8B")));
        sign.getSide(Side.FRONT).line(3, Component.text(""));
        sign.update(true);
    }
}
