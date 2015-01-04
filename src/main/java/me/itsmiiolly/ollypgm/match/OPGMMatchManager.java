package me.itsmiiolly.ollypgm.match;

import java.io.File;
import java.util.HashMap;

import javax.annotation.Nullable;

import me.itsmiiolly.ollypgm.OPGMPlayer;
import me.itsmiiolly.ollypgm.match.OPGMMatch.OPGMMatchState;
import me.itsmiiolly.ollypgm.util.FileUtils;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.google.common.collect.Maps;

/**
 * Manages currently active {@link OPGMMatch}es
 * @author molenzwiebel
 */
public class OPGMMatchManager {
    private int matchId;
    private HashMap<World, OPGMMatch> matches;

    public OPGMMatchManager() {
        this.matches = Maps.newHashMap();
        this.matchId = 0;
    }

    /**
     * Tries to find the match currently playing in the provided world
     * @param world the world for the match
     * @return the match in the provided world, or null if no match is currently active
     */
    @Nullable
    public OPGMMatch getMatch(World world) {
        return matches.get(world);
    }

    /**
     * Tries to find the match with the provided id
     * @param matchid the id of the match
     * @return the match for the specified id, or null if not found
     */
    @Nullable
    public OPGMMatch getMatch(int matchid) {
        OPGMMatch retval = null;
        for (OPGMMatch match : matches.values()) {
            if (match.getMatchID() == matchid) {
                retval = match;
            }
        }
        return retval;
    }

    /**
     * Disables a match, unloading the world and restoring all player states to their previous state
     * @param match the match to unload
     */
    public void unloadMatch(OPGMMatch match) {
        if (match.getState() == OPGMMatchState.ACTIVE || match.getState() == OPGMMatchState.STARTING) throw new IllegalArgumentException("Unloading a match cannot be done if the match is in progress or starting");
        for (OPGMPlayer matchParticipant : match.getParticipatingPlayers()) {
            matchParticipant.setMatch(null);
            matchParticipant.getPreviousState().restore(matchParticipant.getHandle());
            matchParticipant.setPreviousState(null);
        }
        File matchDirectory = match.getMatchWorld().getWorldFolder();
        Bukkit.unloadWorld(match.getMatchWorld(), true);
        try {
            FileUtils.deleteRecursive(matchDirectory);
        } catch (Exception e) {
            throw new RuntimeException("Could not unload match "+match+": Error while deleting match files", e);
        }

        System.out.println("[OllyPGM] Unloaded " + match.getMap().getMapInfo().getDisplayName() + " v" + match.getMap().getMapInfo().getVersion());
        this.matches.remove(match.getMatchWorld());
    }
}
