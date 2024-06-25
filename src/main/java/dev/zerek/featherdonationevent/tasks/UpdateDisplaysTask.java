package dev.zerek.featherdonationevent.tasks;

import com.destroystokyo.paper.profile.PlayerProfile;
import dev.zerek.featherdonationevent.FeatherDonationEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
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

    @Override
    public void run() {
        plugin.getLogger().info("Updating displays.");
        List<String> displayedDonorsList = new ArrayList<>();

        // Set new stand skull and sign for each display

        OfflinePlayer donor1 = getRandomDonor(plugin.getDonors());
        plugin.removeDonorFromPool(donor1);
        displayedDonorsList.add(donor1.getName());
        setDisplay(plugin.getArmorStand1(), plugin.getSign1(), donor1);

        OfflinePlayer donor2 = getRandomDonor(plugin.getDonors());
        plugin.removeDonorFromPool(donor2);
        displayedDonorsList.add(donor2.getName());
        setDisplay(plugin.getArmorStand2(), plugin.getSign2(), donor2);

        OfflinePlayer donor3 = getRandomDonor(plugin.getDonors());
        plugin.removeDonorFromPool(donor3);
        displayedDonorsList.add(donor3.getName());
        setDisplay(plugin.getArmorStand3(), plugin.getSign3(), donor3);

        plugin.getServer().broadcast(MiniMessage.miniMessage().deserialize(generalDisplayUpdated, Placeholder.unparsed("donors", String.join(" ",displayedDonorsList))));
    }

    private OfflinePlayer getRandomDonor(ArrayList<OfflinePlayer> donors) {
        return donors.get(new Random().nextInt(donors.size()));
    }

    private void setDisplay(ArmorStand armorStand, Sign sign, OfflinePlayer donor) {
        String donorName = donor.getName();

        Skull skull = (Skull) armorStand.getLocation().add(0.0, 2.0, 0.0).getBlock();
        BlockFace facing = ((Directional) sign.getBlockData()).getFacing();
        plugin.getLogger().info("Facing: " + facing.name());

        skull.setPlayerProfile(donor.getPlayerProfile());
        Rotatable rotatable = (Rotatable) skull.getBlockData();
        rotatable.setRotation(facing.getOppositeFace());
        skull.setBlockData(rotatable);
        skull.update();

//        // Create the skull item with the donor's profile
//        ItemStack skullItem = new ItemStack(Material.PLAYER_HEAD);
//        SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();
//        skullMeta.setOwningPlayer(donor);
//        skullItem.setItemMeta(skullMeta);
//
//        // Set the block type to player head
//        headBlock.setType(Material.PLAYER_HEAD);
//
//        // Update the block state with the skull meta
//        Skull skull = (Skull) headBlock.getState();
//        skull.setPlayerProfile(donor.getPlayerProfile());
//        skull.update(true);
//
//        // Apply rotation
//        Rotatable rotatable = (Rotatable) skull.getBlockData();
//        rotatable.setRotation(facing.getOppositeFace());
//        skull.setBlockData(rotatable);
//        skull.update(true);
//
//        // Schedule a delayed task to reapply the player profile
//        Bukkit.getScheduler().runTaskLater(plugin, () -> {
//            Skull delayedSkull = (Skull) headBlock.getState();
//            delayedSkull.setPlayerProfile(donor.getPlayerProfile());
//            delayedSkull.update(true);
//            plugin.getLogger().info("Delayed update applied to player profile for donor: " + donorName);
//        }, 20L); // Delay of 1 second (20 ticks)

        sign.getSide(Side.FRONT).line(0, Component.text(""));
        sign.getSide(Side.FRONT).line(1, Component.text(donorName).color(TextColor.fromHexString("#FFFFFF")));
        sign.getSide(Side.FRONT).line(2, Component.text("Donor").color(TextColor.fromHexString("#FFEB8B")));
        sign.getSide(Side.FRONT).line(3, Component.text(""));
        sign.update(true);
    }

}