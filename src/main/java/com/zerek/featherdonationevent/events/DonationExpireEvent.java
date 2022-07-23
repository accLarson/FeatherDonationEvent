package com.zerek.featherdonationevent.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class DonationExpireEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    //{purchaserName} {purchaserUuid} {username} {id} {packageName} {price}
    private final String purchaserName;
    private final String purchaserUuid;
    private final String username;
    private final String id;
    private final String packageName;
    private final String price;

    public DonationExpireEvent(String purchaserName, String purchaserUuid, String username, String id, String packageName, String price){
        this.purchaserName = purchaserName;
        this.purchaserUuid = purchaserUuid;
        this.username = username;
        this.id = id;
        this.packageName = packageName;
        this.price = price;
    }

    public String getPurchaserName() {
        return purchaserName;
    }

    public String getPurchaserUuid() {
        return purchaserUuid;
    }

    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getPrice() {
        return price;
    }




    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
