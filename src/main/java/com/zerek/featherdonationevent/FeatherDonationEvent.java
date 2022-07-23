package com.zerek.featherdonationevent;

import com.zerek.featherdonationevent.commands.*;
import com.zerek.featherdonationevent.listeners.DonationListener;
import com.zerek.featherdonationevent.tasks.UpdateDisplaysTask;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public final class FeatherDonationEvent extends JavaPlugin {

    private final HashMap<String, Location> newDisplay = new HashMap<>();
    private final HashMap<Integer, HashMap<String, Location>> generalDisplays = new HashMap<>();

    private ArrayList<OfflinePlayer> donors = new ArrayList<OfflinePlayer>();
    private Permission perms = null;
    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getCommand("alertdonation").setExecutor(new AlertDonationCommand(this));
        this.getCommand("alertdonationexpire").setExecutor(new AlertDonationExpireCommand(this));
        this.getCommand("alertsupporter").setExecutor(new AlertSupporterCommand(this));
        this.getCommand("alertsupporterrenew").setExecutor(new AlertSupporterRenewCommand(this));
        this.getCommand("donordisplay").setExecutor(new DonorDisplayCommand(this));
        this.getCommand("donordisplay").setTabCompleter(new DonorDisplayTabCompleter());

        this.saveDefaultConfig();

        List<Integer> newStandConfig = getConfig().getIntegerList("newest.stand");
        List<Integer> newSignConfig = getConfig().getIntegerList("newest.sign");
        newDisplay.put("stand",new Location(getServer().getWorld("world"),newStandConfig.get(0),newStandConfig.get(1),newStandConfig.get(2)));
        newDisplay.put("sign",new Location(getServer().getWorld("world"),newSignConfig.get(0),newSignConfig.get(1),newSignConfig.get(2)));

        getConfig().getConfigurationSection("general").getKeys(false).forEach(d -> {
            HashMap<String, Location> locations = new HashMap<>();
            List<Integer> standConfig = getConfig().getIntegerList("general." + d + ".stand");
            List<Integer> signConfig = getConfig().getIntegerList("general." + d + ".sign");
            locations.put("stand",new Location(getServer().getWorld("world"),standConfig.get(0),standConfig.get(1),standConfig.get(2)));
            locations.put("sign",new Location(getServer().getWorld("world"),signConfig.get(0),signConfig.get(1),signConfig.get(2)));
            generalDisplays.put(Integer.valueOf(d),locations);
        });

        perms = getServer().getServicesManager().getRegistration(Permission.class).getProvider();
        donors = findAllDonors();
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new UpdateDisplaysTask(this), 2400L, 288000L);
        getServer().getPluginManager().registerEvents(new DonationListener(this),this);
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private ArrayList<OfflinePlayer> findAllDonors(){
        donors.clear();
        getLogger().info("Checking " + getServer().getOfflinePlayers().length + " players for group donor.");
        Arrays.stream(getServer().getOfflinePlayers()).filter(p -> perms.playerInGroup(null, p, "donor")).forEach(p -> donors.add(p));
        getLogger().info("Found " + donors.size() + " donors.");
        return donors;
    }

    public void removeDonor(OfflinePlayer donor){
        donors.remove(donor);
    }
    public ArrayList<OfflinePlayer> getDonors() {
        return donors;
    }
    public HashMap<String, Location> getNewDisplay() {
        return newDisplay;
    }
    public HashMap<Integer, HashMap<String, Location>> getGeneralDisplays() {
        return generalDisplays;
    }
}
