package me.itsmiiolly.ollypgm;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import me.itsmiiolly.ollypgm.match.OPGMMatch;
import me.itsmiiolly.ollypgm.team.OPGMTeam;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Player object for OllyPGM, wraps around a Bukkit {@link Player}. Note that a OPGMPlayer does not have to be in a match.
 * @author molenzwiebel
 */
public class OPGMPlayer {
    private static final HashMap<String, OPGMPlayer> cache = Maps.newHashMap();
    
    private Player handle;
    private @Nullable OPGMMatch match;
    private @Nullable OPGMTeam team;
    
    private @Nullable OPGMPlayerState previousState;
    
    OPGMPlayer(Player handle) {
        this.handle = handle;
        
        cache.put(handle.getName(), this);
    }
    
    /**
     * @return the corresponding {@link Player} for this instance
     */
    public Player getHandle() {
        return handle;
    }
    
    /**
     * @return if the player is currently participating in any kind of {@link OPGMMatch}
     */
    public boolean isParticipating() {
        return getMatch() != null;
    }
    
    /**
     * @return the match this player is in, or <code>null</code> if the player is not in a match
     */
    @Nullable
    public OPGMMatch getMatch() {
        return match;
    }
    
    /**
     * Sets the match for this player
     * @param newMatch the new match, or null if not in a match
     */
    public void setMatch(@Nullable OPGMMatch newMatch) {
        this.match = newMatch;
    }
    
    /**
     * @return the team this player is in, or <code>null</code> if the player is not participating in a match
     */
    @Nullable
    public OPGMTeam getTeam() {
        return team;
    }
    
    /**
     * @return the {@link OPGMPlayerState} this {@link OPGMPlayer} was in before entering the match. Returns null if player is not currently participating
     */
    @Nullable
    public OPGMPlayerState getPreviousState() {
        return previousState;
    }
    
    /**
     * Sets the captured player state for this player
     * @param newState the new state, or null if not in a match
     */
    public void setPreviousState(@Nullable OPGMPlayerState newState) {
        this.previousState = newState;
    }
    
    /**
     * Gets the {@link OPGMPlayer} instance for the provided {@link Player} handle
     * @param handle the Bukkit {@link Player} instance
     * @return the {@link OPGMPlayer} instance
     */
    public static OPGMPlayer from(Player handle) {
        if (cache.containsKey(handle.getName())) {
            return cache.get(handle.getName());
        }
        //Constructor registers it into cache
        return new OPGMPlayer(handle);
    }
    
    /**
     * Creates or loads a {@link OPGMPlayer} instance for every {@link Player} currently online
     * @return all players online
     */
    public static Collection<OPGMPlayer> getPlayers() {
        List<OPGMPlayer> retval = Lists.newArrayList();
        for (Player bukkitPlayer : Bukkit.getOnlinePlayers()) {
            retval.add(OPGMPlayer.from(bukkitPlayer));
        }
        return ImmutableList.copyOf(retval);
    }
    
    /**
     * Gets all {@link OPGMPlayer} instances currently in the provided {@link OPGMMatch}
     * @param match the match
     * @return all player instances
     */
    public static Collection<OPGMPlayer> getPlayers(OPGMMatch match) {
        List<OPGMPlayer> retval = Lists.newArrayList();
        for (OPGMPlayer player : getPlayers()) {
            if (player.isParticipating() && player.getMatch().equals(match)) {
                retval.add(player);
            }
        }
        return retval;
    }
    
    /**
     * Gets all {@link OPGMPlayer} instances currently in the provided {@link OPGMTeam}
     * @param team the team
     * @return all player instances
     */
    public static Collection<OPGMPlayer> getPlayers(OPGMTeam team) {
        List<OPGMPlayer> retval = Lists.newArrayList();
        for (OPGMPlayer player : getPlayers()) {
            if (player.isParticipating() && player.getTeam().equals(team)) {
                retval.add(player);
            }
        }
        return retval;
    }
}
