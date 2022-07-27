package com.zerek.featherdonationevent.tasks;

import com.zerek.featherdonationevent.FeatherDonationEvent;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.entity.ArmorStand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InitiateTask implements Runnable{

    private final FeatherDonationEvent plugin;

    public InitiateTask(FeatherDonationEvent plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        World world = plugin.getServer().getWorlds().get(0);

        // Register newest donor display.
        List<Integer> newestStandConfig = this.plugin.getConfig().getIntegerList("newest.stand");
        List<Integer> newestSignConfig = this.plugin.getConfig().getIntegerList("newest.sign");
        Location newestStandLoc = new Location(world, newestStandConfig.get(0), newestStandConfig.get(1), newestStandConfig.get(2));
        Location newestSignLoc = new Location(world, newestSignConfig.get(0), newestSignConfig.get(1), newestSignConfig.get(2));

        // Register general donor displays.
        Map<Integer,List<Integer>> generalStandConfigs = new HashMap<>();
        Map<Integer,List<Integer>> generalSignConfigs = new HashMap<>();
        this.plugin.getConfig().getConfigurationSection("general").getKeys(false).forEach(k -> {
            generalStandConfigs.put(Integer.valueOf(k),plugin.getConfig().getIntegerList("general." + k + ".stand"));
            generalSignConfigs.put(Integer.valueOf(k),plugin.getConfig().getIntegerList("general." + k + ".sign"));
        });
        Map<Integer, Location> generalStandLocs = new HashMap<>();
        Map<Integer, Location> generalSignLocs = new HashMap<>();
        generalStandConfigs.keySet().forEach(k -> generalStandLocs.put(k,new Location(world, generalStandConfigs.get(k).get(0), generalStandConfigs.get(k).get(1), generalStandConfigs.get(k).get(2))));
        generalStandConfigs.keySet().forEach(k -> generalSignLocs.put(k,new Location(world, generalSignConfigs.get(k).get(0), generalSignConfigs.get(k).get(1), generalSignConfigs.get(k).get(2))));

        // Check if stands are loaded.
        if (newestStandLoc.getChunk().isEntitiesLoaded() && generalStandLocs.keySet().stream().allMatch(k -> generalStandLocs.get(k).getChunk().isEntitiesLoaded())) {

            // Check for newest armor stand and sign at specified location and set if found.
            List<ArmorStand> newestStandList = (List<ArmorStand>) newestStandLoc.toCenterLocation().getNearbyEntitiesByType(ArmorStand.class, 0.5,1.0,0.5);
            if (newestStandList.size() > 0) {
                if (newestSignLoc.getBlock().getState() instanceof Sign){
                    plugin.getLogger().info("Newest armor stand donor display and sign found.");
                    plugin.getNewestDisplay().put(newestStandList.get(0), (Sign) newestSignLoc.getBlock().getState());
                }
            }
            else plugin.getLogger().warning("No newest armor stand donor display found at: " + newestStandLoc.getBlockX() + " " + newestStandLoc.getBlockY() + " " + newestStandLoc.getBlockZ() + ".");

            // Check for general armor stands and signs at specified locations and set if found.
            generalStandLocs.keySet().forEach(k -> {
                List<ArmorStand> generalStandList = (List<ArmorStand>) generalStandLocs.get(k).toCenterLocation().getNearbyEntitiesByType(ArmorStand.class, 0.5,1.0,0.5);
                if (generalStandList.size() > 0) {
                    if (generalSignLocs.get(k).getBlock().getState() instanceof Sign){
                        plugin.getLogger().info("General armor stand donor display and sign " + k + " found.");
                        Map<ArmorStand,Sign> display = new HashMap<>();
                        display.put(generalStandList.get(0), (Sign) generalSignLocs.get(k).getBlock().getState());
                        plugin.getGeneralDisplays().put(k,display);
                    }
                }
                else plugin.getLogger().warning("No General armor stand donor display found at: " + newestStandLoc.getBlockX() + " " + newestStandLoc.getBlockY() + " " + newestStandLoc.getBlockZ() + ".");
            });
        }

        // Check for failed stand or sign assignment and schedule to attempt again in 10 seconds.
        if (plugin.getNewestDisplay().size() != 1 || plugin.getGeneralDisplays().size() != generalStandConfigs.size()) {
            plugin.getServer().getScheduler().runTaskLater(plugin, new InitiateTask(plugin),200L);
        }
        // All checks passed, donor display starting.
        else plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new UpdateDisplaysTask(plugin), 0L, 288000L);
    }
}
