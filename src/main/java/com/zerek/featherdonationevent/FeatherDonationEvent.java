package com.zerek.featherdonationevent;

import com.zerek.featherdonationevent.commands.*;
import com.zerek.featherdonationevent.listeners.DonationListener;
import com.zerek.featherdonationevent.tasks.InitiateTask;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class FeatherDonationEvent extends JavaPlugin {

    private final Map<ArmorStand, Sign> newestDisplay = new HashMap<>();
    private final Map<Integer, Map<ArmorStand, Sign>> generalDisplays = new HashMap<>();

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

        this.perms = getServer().getServicesManager().getRegistration(Permission.class).getProvider();
        this.donors = findAllDonors();
        getServer().getScheduler().runTaskLater(this, new InitiateTask(this),200L);
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

    public Map<ArmorStand, Sign> getNewestDisplay() {
        return newestDisplay;
    }

    public Map<Integer, Map<ArmorStand, Sign>> getGeneralDisplays() {
        return generalDisplays;
    }
}
