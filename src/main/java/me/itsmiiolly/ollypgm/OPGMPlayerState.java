package me.itsmiiolly.ollypgm;

import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.google.common.collect.Lists;

/**
 * Represents the state a player can be in. This includes world, gamemode, inventory, statistics and buffs. Used for restoring a player's state once they leave a match
 * @author molenzwiebel
 */
public class OPGMPlayerState {
    private ItemStack[] inventory;
    private ItemStack[] armorSlots;

    private List<PotionEffect> potionEffects;
    private GameMode playerGamemode;
    private Location playerLocation;
    
    private double health;
    private double maxHealth;
    private int food;
    private float saturation;
    private int fire;
    
    //Remove public constructor
    OPGMPlayerState() {}
    
    /**
     * Restores the OPGMPlayerState to the provided player
     * @param target the player to restore the saved information to
     */
    public void restore(Player target) {
        target.getInventory().setContents(inventory);
        target.getInventory().setArmorContents(armorSlots);
        
        for (PotionEffect existingPotionEffect : target.getActivePotionEffects()) {
            target.removePotionEffect(existingPotionEffect.getType());
        }
        for (PotionEffect potionEffect : potionEffects) {
            target.addPotionEffect(potionEffect);
        }
        target.setGameMode(playerGamemode);
        target.teleport(playerLocation);
        
        target.setHealth(health);
        target.setMaxHealth(maxHealth);
        target.setFoodLevel(food);
        target.setSaturation(saturation);
        target.setFireTicks(fire);
    }
    
    /**
     * Makes a snapshot of the provided player's state and returns the corresponding OPGMPlayerState
     * @param player the player to create a OPGMPlayerState for
     * @return the snapshot
     */
    public static OPGMPlayerState create(Player player) {
        OPGMPlayerState ret = new OPGMPlayerState();
        
        ret.inventory = player.getInventory().getContents();
        ret.armorSlots = player.getInventory().getArmorContents();
        
        ret.potionEffects = Lists.newArrayList(player.getActivePotionEffects());
        ret.playerGamemode = player.getGameMode();
        ret.playerLocation = player.getLocation();
        
        ret.health = player.getHealth();
        ret.maxHealth = player.getMaxHealth();
        ret.food = player.getFoodLevel();
        ret.saturation = player.getSaturation();
        ret.fire = player.getFireTicks();
        
        return ret;
    }
}
