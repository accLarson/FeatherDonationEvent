package dev.zerek.featherdonationevent;

import dev.zerek.featherdonationevent.commands.*;
import dev.zerek.featherdonationevent.listeners.DonationListener;
import dev.zerek.featherdonationevent.tasks.InitiateTask;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class FeatherDonationEvent extends JavaPlugin {

    private ArmorStand armorStand1;
    private ArmorStand armorStand2;
    private ArmorStand armorStand3;
    private ArmorStand armorStandNew;
    private Sign sign1;
    private Sign sign2;
    private Sign sign3;
    private Sign signNew;
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
        this.getLogger().info("Attempting to start Donor Display.");
        getServer().getScheduler().runTaskLater(this, new InitiateTask(this), 200L);
        getServer().getPluginManager().registerEvents(new DonationListener(this), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ArrayList<OfflinePlayer> findAllDonors() {
        donors.clear();
        getLogger().info("Checking " + getServer().getOfflinePlayers().length + " players for group donor.");
        Arrays.stream(getServer().getOfflinePlayers()).filter(p -> perms.playerInGroup(null, p, "donor")).forEach(p -> donors.add(p));
        getLogger().info("Found " + donors.size() + " donors.");
        return donors;
    }

    public void removeDonorFromPool(OfflinePlayer donor) {
        donors.remove(donor);
    }

    public ArrayList<OfflinePlayer> getDonors() {
        return donors;
    }

    public ArmorStand getArmorStand1() {
        return armorStand1;
    }

    public void setArmorStand1(ArmorStand armorStand1) {
        this.armorStand1 = armorStand1;
    }

    public ArmorStand getArmorStand2() {
        return armorStand2;
    }

    public void setArmorStand2(ArmorStand armorStand2) {
        this.armorStand2 = armorStand2;
    }

    public ArmorStand getArmorStand3() {
        return armorStand3;
    }

    public void setArmorStand3(ArmorStand armorStand3) {
        this.armorStand3 = armorStand3;
    }

    public ArmorStand getArmorStandNew() {
        return armorStandNew;
    }

    public void setArmorStandNew(ArmorStand armorStandNew) {
        this.armorStandNew = armorStandNew;
    }

    public Sign getSign1() {
        return sign1;
    }

    public void setSign1(Sign sign1) {
        this.sign1 = sign1;
    }

    public Sign getSign2() {
        return sign2;
    }

    public void setSign2(Sign sign2) {
        this.sign2 = sign2;
    }

    public Sign getSign3() {
        return sign3;
    }

    public void setSign3(Sign sign3) {
        this.sign3 = sign3;
    }

    public Sign getSignNew() {
        return signNew;
    }

    public void setSignNew(Sign signNew) {
        this.signNew = signNew;
    }
}
