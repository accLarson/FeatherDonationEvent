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
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.ArmorStand;

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