package me.itsmiiolly.ollypgm.map;

import java.util.Collection;
import java.util.List;

import me.itsmiiolly.ollypgm.util.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.World.Environment;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class OPGMMapInfo {
    private String name;
    private String version;
    private String objective;
    private List<OPGMContributor> authors;
    private List<OPGMContributor> contributors;
    private List<String> rules;
    private Difficulty difficulty;
    private Environment dimension;
    private boolean friendlyFire;

    OPGMMapInfo() { }

    public String getShortDescription() {
        String authorsString = StringUtils.listToEnglishCompound(this.authors, ChatColor.RED.toString(), ChatColor.DARK_PURPLE.toString());
        return ChatColor.GOLD + this.name + ChatColor.DARK_PURPLE + " by " + authorsString;
    }

    public boolean allowsFriendlyFire() {
        return friendlyFire;
    }

    public List<OPGMContributor> getAuthors() {
        return ImmutableList.copyOf(authors);
    }

    public List<OPGMContributor> getContributors() {
        return ImmutableList.copyOf(contributors);
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public Environment getDimension() {
        return dimension;
    }

    public String getDisplayName() {
        return name;
    }

    public String getObjective() {
        return objective;
    }

    public List<String> getRules() {
        return ImmutableList.copyOf(rules);
    }

    public String getVersion() {
        return version;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("OPGMMapInfo [name=%s, version=%s, objective=%s, authors=%s, contributors=%s, rules=%s, difficulty=%s, dimension=%s, friendlyFire=%s]",
                        name, version, objective, authors, contributors, rules,
                        difficulty, dimension, friendlyFire);
    }

    /**
     * Builder class for {@link OPGMMapInfo}.<br>
     * Sample usage:<br>
     * <code>OPGMMapInfo map = new OPGMMapInfo.Builder().name("Map Name").version("1").build();</code>
     * @author molenzwiebel
     */
    public static class Builder {
        private OPGMMapInfo instance = new OPGMMapInfo();
        
        public Builder name(String name) {
            instance.name = name;
            return this;
        }
        
        public Builder version(String version) {
            instance.version = version;
            return this;
        }
        
        public Builder objective(String objective) {
            instance.objective = objective;
            return this;
        }
        
        public Builder authors(Collection<OPGMContributor> authors) {
            instance.authors = Lists.newArrayList(authors);
            return this;
        }
        
        public Builder contributors(Collection<OPGMContributor> contributors) {
            instance.contributors = Lists.newArrayList(contributors);
            return this;
        }
        
        public Builder rules(Collection<String> rules) {
            instance.rules = Lists.newArrayList(rules);
            return this;
        }
        
        public Builder difficulty(Difficulty diff) {
            instance.difficulty = diff;
            return this;
        }
        
        public Builder dimension(Environment dim) {
            instance.dimension = dim;
            return this;
        }
        
        public Builder friendlyFire(boolean ff) {
            instance.friendlyFire = ff;
            return this;
        }
        
        public OPGMMapInfo build() {
            return instance;
        }
    }
}
