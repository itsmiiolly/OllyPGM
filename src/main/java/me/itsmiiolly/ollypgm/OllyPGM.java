package me.itsmiiolly.ollypgm;

import me.itsmiiolly.ollypgm.intake.BukkitAuthorization;
import me.itsmiiolly.ollypgm.intake.BukkitCommandRegistration;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.sk89q.intake.CommandException;
import com.sk89q.intake.InvalidUsageException;
import com.sk89q.intake.InvocationCommandException;
import com.sk89q.intake.context.CommandLocals;
import com.sk89q.intake.dispatcher.Dispatcher;
import com.sk89q.intake.fluent.CommandGraph;
import com.sk89q.intake.parametric.ParametricBuilder;
import com.sk89q.intake.parametric.handler.LegacyCommandsHandler;
import com.sk89q.intake.util.auth.AuthorizationException;

public class OllyPGM extends JavaPlugin {
    private Dispatcher commandDispatcher;
    
    @Override
    public void onEnable() {
        this.registerCommands();
    }

    @Override
    public void onDisable() {
    }
    
    /**
     * Setups and registers every command in the {@link #commandDispatcher}
     */
    public void registerCommands() {
        ParametricBuilder builder = new ParametricBuilder();
        builder.addInvokeListener(new LegacyCommandsHandler());
        builder.setAuthorizer(new BukkitAuthorization());

        CommandGraph cmdGraph = new CommandGraph().builder(builder);
        commandDispatcher = cmdGraph.getDispatcher();
        
        BukkitCommandRegistration.register(this, commandDispatcher.getCommands());
    }
    
    /**
     * Funnel bukkit commands to the command framework.
     */
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String commandLabel, String[] args) {
        try {
            CommandLocals locals = new CommandLocals();
            locals.put(CommandSender.class, sender); //save sender
            String[] parentCommands = new String[0]; // No parent commands
            this.commandDispatcher.call(cmd.getName() + " " + Joiner.on(" ").join(args), locals, parentCommands);
        } catch (Exception e) {
            handleException(sender, e);
        }
        return true;
    }

    /**
     * Handles any exception thrown by {@link #onCommand(CommandSender, org.bukkit.command.Command, String, String[])}
     * @param sender the command sender
     * @param e the exception
     */
    private void handleException(CommandSender sender, Throwable e) {
        if (e instanceof InvocationCommandException) {
            handleException(sender, e.getCause());
        } else if (e instanceof NumberFormatException) {
            sender.sendMessage(ChatColor.RED + "Number expected, string received instead");
        } else if (e instanceof IllegalArgumentException) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
        } else if (e instanceof NullPointerException && e.getStackTrace()[0].getClassName().equals(Preconditions.class.getName())) { //Hacky 
            sender.sendMessage(ChatColor.RED + e.getMessage());
        } else if (e instanceof AuthorizationException) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use that command!");
        } else if (e instanceof InvalidUsageException) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            sender.sendMessage(ChatColor.RED + ((InvalidUsageException) e).getSimpleUsageString("/"));
        } else if (e instanceof CommandException) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
        } else {
            if (e.getCause() == null) {
                sender.sendMessage(ChatColor.RED + "Unexpected error encountered. Check the console logs");
                e.printStackTrace();
            } else {
                handleException(sender, e.getCause());
            }
        }
    }
}
