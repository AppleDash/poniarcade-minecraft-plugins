package com.poniarcade.classesng.classes;

import com.poniarcade.classesng.classes.power.SaddlePower;

import java.util.Optional;
import java.util.Set;

/**
 * Created by appledash on 8/12/16.
 * Blackjack is still best pony.
 */
public class PlayerClassData {
    private Class playerClass;               // Their current class
    private Class pendingClassPurchase;      // The class we are waiting on confirmation to buy with /class buy confirm
    private long pendingClassPurchaseExpiry; // When the above confirmation times out
    private SaddlePower activeSaddlePower;   // The player's active Saddle Power
    private long saddlePowerExpiry;          // When the active saddle power becomes inactive
    private long nextClassChange;            // When the player can next change classes
    private Class spoofedClass;              // Their "spoofed" class, used for Draconequus
    private long spoofedClassExpiry;         // When their "spoofed" class expires.
    private Set<Class> purchasedClasses;     // Master classes that the player has already purchased

    private boolean dirty;

    public PlayerClassData(Class playerClass, long nextClassChange, Class spoofedClass, long spoofedClassExpiry, Set<Class> purchasedClasses) {
        this.playerClass = playerClass;
        this.nextClassChange = nextClassChange;
        this.spoofedClass = spoofedClass;
        this.spoofedClassExpiry = spoofedClassExpiry;
        this.purchasedClasses = purchasedClasses;
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public void merge(PlayerClassData other) {
        this.playerClass = other.playerClass;
        this.nextClassChange = other.nextClassChange;
        if (this.spoofedClassExpiry != -1) {
            this.spoofedClass = other.spoofedClass;
            this.spoofedClassExpiry = other.spoofedClassExpiry;
        }
        if (!other.purchasedClasses.isEmpty()) {
            this.purchasedClasses = other.purchasedClasses;
        }
    }

    public Optional<Class> getPlayerClass() {
        return Optional.ofNullable(this.playerClass);
    }

    public void setPlayerClass(Class playerClass) {
        this.playerClass = playerClass;
        this.dirty = true;
    }

    public void setNextClassChange(long nextClassChange) {
        this.nextClassChange = nextClassChange;
        this.dirty = true;
    }

    public void setActiveSaddlePower(SaddlePower saddlePower, long expiryTime) {
        this.activeSaddlePower = saddlePower;
        this.saddlePowerExpiry = expiryTime;
    }

    public boolean isSaddlePowerActive() {
        return this.activeSaddlePower != null;
    }

    public boolean isSaddlePowerExpired() {
        return (this.activeSaddlePower != null) && (System.currentTimeMillis() >= this.saddlePowerExpiry);
    }

    public void deactivateSaddlePower() {
	    this.activeSaddlePower = null;
	    this.saddlePowerExpiry = -1;
    }

    public SaddlePower getActiveSaddlePower() {
        return this.activeSaddlePower;
    }

    public void setPendingPurchase(Class pendingPurchase, long expiry) {
        this.pendingClassPurchase = pendingPurchase;
        this.pendingClassPurchaseExpiry = expiry;
    }

    public Class getPendingPurchase() {
        return this.pendingClassPurchase;
    }

    public void removePendingPurchase() {
        this.pendingClassPurchase = null;
    }

    public long getNextClassChange() {
        return this.nextClassChange;
    }

    public void removeNextClassChange() {
	    this.nextClassChange = -1;
        this.dirty = true;
    }

    public long getTimeUntilPendingPurchase() {
        return System.currentTimeMillis() - this.pendingClassPurchaseExpiry;
    }

    public void setSpoofedClass(Class spoofed, long expiry) {
        this.spoofedClass = spoofed;
        this.spoofedClassExpiry = expiry;
        this.dirty = true;
    }

    public Optional<Class> getSpoofedClass() {
        return Optional.ofNullable(this.spoofedClass);
    }

    public long getSpoofedClassExpiry() {
        return this.spoofedClassExpiry;
    }

    public void removeSpoofedClass() {
	    this.spoofedClass = null;
	    this.spoofedClassExpiry = -1;
        this.dirty = true;
    }

    public Set<Class> getPurchasedClasses() {
        return this.purchasedClasses;
    }

    public void addPurchasedClass(Class clazz) {
	    this.purchasedClasses.add(clazz);
        this.dirty = true;
    }

    public void removePlayerClass() {
        this.setPlayerClass(null);
    }
}
