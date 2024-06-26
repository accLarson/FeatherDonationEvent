package dev.zerek.featherdonationevent.listeners;

import com.destroystokyo.paper.profile.PlayerProfile;
import dev.zerek.featherdonationevent.FeatherDonationEvent;
import dev.zerek.featherdonationevent.events.DonationEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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

        Block blockAbove = armorStand.getLocation().add(0, 2, 0).getBlock();
        blockAbove.setType(Material.PLAYER_HEAD);
        PlayerProfile playerProfile = Bukkit.createProfile(donor.getUniqueId(),donorName);
        Skull skull = (Skull) blockAbove.getState();
        BlockFace facing = ((Directional) sign.getBlockData()).getFacing();
        Rotatable rotatable = (Rotatable) skull.getBlockData();
        rotatable.setRotation(facing.getOppositeFace());
        skull.setBlockData(rotatable);

        playerProfile.update().thenAcceptAsync(profile -> {
            skull.setPlayerProfile(profile);
            skull.update(true);
        }, Bukkit.getScheduler().getMainThreadExecutor(plugin));

        sign.getSide(Side.FRONT).line(0, Component.text(""));
        sign.getSide(Side.FRONT).line(1, Component.text(donorName).color(TextColor.fromHexString("#FFFFFF")));
        sign.getSide(Side.FRONT).line(2, Component.text("Donor").color(TextColor.fromHexString("#FFEB8B")));
        sign.getSide(Side.FRONT).line(3, Component.text(""));
        sign.update(true);
    }
}
