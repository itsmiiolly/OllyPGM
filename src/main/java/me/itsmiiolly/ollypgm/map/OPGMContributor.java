package me.itsmiiolly.ollypgm.map;

import javax.annotation.Nullable;

import com.google.common.base.Optional;
import me.itsmiiolly.ollypgm.util.StringUtils;

public class OPGMContributor {
    private String name;
    private Optional<String> contribution;

    public OPGMContributor(String name, @Nullable String contribution) {
        this.name = name;
        this.contribution = Optional.fromNullable(contribution);
    }
    
    /**
     * Gets the contributor's name
     * @return the contributor's name, usually a minecraft username
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the contribution, which can be empty
     * @return the contribution
     */
    public Optional<String> getContribution() {
        return contribution;
    }

    /**
     * @return if any contribution is present
     */
    public boolean hasContribution() {
        return this.getContribution().isPresent();
    }
    
    /**
     * Overrides toString so {@link StringUtils#listToEnglishCompound(java.util.Collection, String, String)} works correctly for contributors.
     */
    @Override
    public String toString() {
        return getName();
    }
}
