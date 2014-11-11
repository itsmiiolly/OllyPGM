package me.itsmiiolly.ollypgm.team;

import me.itsmiiolly.ollypgm.match.OPGMMatch;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

/**
 * Represents a team in a {@link OPGMMatch}
 * @author molenzwiebel
 */
public class OPGMTeam {
    private final String initialName;
    private String name;
    
    private final ChatColor initialColor;
    private ChatColor color;
    
    private int maxSize;
    private final OPGMTeamType type;
    
    private final OPGMMatch match;
    private final Team scoreboardTeam;

    public OPGMTeam(OPGMMatch match, Team scoreboardTeam, OPGMTeamType type, String name, ChatColor color, int size) {
        this.type = type;
        this.name = name;
        this.initialName = name;
        this.color = color;
        this.initialColor = color;
        this.maxSize = size;
        this.match = match;
        this.scoreboardTeam = scoreboardTeam;
    }

    /**
     * @return the max amount of players on one team
     */
    public int getCapacity() {
        return maxSize;
    }

    /**
     * Sets the color for the team. Does not modify {@link #getInitialColor()}
     * @param newColor the new color
     */
    public void setColor(ChatColor newColor) {
        this.color = newColor;
    }

    /**
     * @return the color of the team
     */
    public ChatColor getColor() {
        return color;
    }

    /**
     * @return the initial color of the team
     */
    public final ChatColor getInitialColor() {
        return initialColor;
    }

    /**
     * @return the initial name of the team
     */
    public final String getInitialName() {
        return initialName;
    }

    /**
     * @return the name of the team
     */
    public String getName() {
        return name;
    }
      
    /**
     * Sets the name for the team. Does not modify {@link #getInitialName()}
     * @param newName the new name
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * @return the display name of the team (color + name)
     */
    public String getDisplayName() {
        return getColor() + getName() + ChatColor.RESET;
    }
    
    /**
     * @return the team type
     */
    public OPGMTeamType getType() {
        return type;
    }
    
    /**
     * @return the match that this team participates in
     */
    public OPGMMatch getMatch() {
        return match;
    }

    /**
     * @return the Bukkit scoreboard {@link Team} for this {@link OPGMTeam}
     */
    public Team getScoreboardTeam() {
        return scoreboardTeam;
    }
    
    /**
     * Represents all possible types a {@link OPGMTeam} can be.
     * @author molenzwiebel
     */
    public enum OPGMTeamType {
        /**
         * The team is participating in a {@link OPGMMatch}, allowing for interaction with other PARTICIPATING teams, as well as the world (if allowed)
         */
        PARTICIPATING,
        /**
         * The team is observing and should be denied any interaction with the {@link OPGMMatch}, players or the world
         */
        OBSERVING;
    }
}
