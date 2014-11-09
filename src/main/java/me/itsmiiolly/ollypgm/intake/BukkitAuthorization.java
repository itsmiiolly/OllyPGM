package me.itsmiiolly.ollypgm.intake;

import org.bukkit.command.CommandSender;

import com.sk89q.intake.context.CommandLocals;
import com.sk89q.intake.util.auth.Authorizer;

public class BukkitAuthorization implements Authorizer {
    /**
     * Checks if the {@link CommandSender} invoking the command has the correct permissions to do so using bukkits SuperPermissions.
     */
    @Override
    public boolean testPermission(CommandLocals locals, String permission) {
        return locals.get(CommandSender.class).hasPermission(permission);
    }
}