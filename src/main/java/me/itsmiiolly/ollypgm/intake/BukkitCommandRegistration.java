package me.itsmiiolly.ollypgm.intake;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicFactory;
import org.bukkit.plugin.Plugin;

import com.google.common.base.Joiner;
import com.sk89q.intake.CommandMapping;

/**
 * Utility class that registers Intake commands in the Bukkit command map. Basically ripped from WorldEdit
 * @author molenzwiebel
 */
public class BukkitCommandRegistration {
    static {
        Bukkit.getServer().getHelpMap().registerHelpTopicFactory(DynamicBukkitCommand.class, new DynamicCommandHelpTopic.Factory());
    }

    public static boolean register(Plugin plugin, Collection<CommandMapping> registered) {
        CommandMap commandMap = getCommandMap();
        if (registered == null || commandMap == null) {
            return false;
        }
        for (CommandMapping command : registered) {
            DynamicBukkitCommand cmd = new DynamicBukkitCommand(command.getAllAliases(), command.getDescription().getShortDescription(), "/" + command.getAllAliases()[0] + " " + command.getDescription().getUsage(), plugin, plugin, plugin);
            cmd.setPermissions(command.getDescription().getPermissions().toArray(new String[1]));
            commandMap.register(plugin.getDescription().getName(), cmd);
        }
        return true;
    }

    public static CommandMap getCommandMap() {
        CommandMap commandMap = getField(Bukkit.getServer().getPluginManager(), "commandMap");
        if (commandMap == null) {
            throw new RuntimeException("Could not get Bukkit commandMap, dynamic command injection failed.");
        }
        return commandMap;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getField(Object from, String name) {
        Class<?> checkClass = from.getClass();
        do {
            try {
                Field field = checkClass.getDeclaredField(name);
                field.setAccessible(true);
                return (T) field.get(from);
            } catch (NoSuchFieldException e) {
            } catch (IllegalAccessException e) { }
        } while (checkClass.getSuperclass() != Object.class && ((checkClass = checkClass.getSuperclass()) != null));
        return null;
    }

    public static class DynamicBukkitCommand extends Command implements PluginIdentifiableCommand {
        protected final CommandExecutor owner;
        protected final Object registeredWith;
        protected final Plugin owningPlugin;
        protected String[] permissions = new String[0];

        public DynamicBukkitCommand(String[] aliases, String desc, String usage, CommandExecutor owner, Object registeredWith, Plugin plugin) {
            super(aliases[0], desc, usage, Arrays.asList(aliases));
            this.owner = owner;
            this.owningPlugin = plugin;
            this.registeredWith = registeredWith;
        }

        @Override
        public boolean execute(CommandSender sender, String label, String[] args) {
            return owner.onCommand(sender, this, label, args);
        }

        public void setPermissions(String[] permissions) {
            this.permissions = permissions;
            if (permissions != null) {
                super.setPermission(Joiner.on(";").useForNull("").join(permissions));
            }
        }

        public String[] getPermissions() {
            return permissions;
        }

        @Override
        public Plugin getPlugin() {
            return owningPlugin;
        }
    }

    public static class DynamicCommandHelpTopic extends HelpTopic {
        private final DynamicBukkitCommand cmd;

        public DynamicCommandHelpTopic(DynamicBukkitCommand cmd) {
            this.cmd = cmd;
            this.name = "/" + cmd.getName();

            String fullTextTemp = null;
            StringBuilder fullText = new StringBuilder();

            this.shortText = cmd.getDescription();

            // Put the usage in the format: Usage string (newline) Aliases (newline) Help text
            String[] split = fullTextTemp == null ? new String[2] : fullTextTemp.split("\n", 2);
            fullText.append(ChatColor.BOLD).append(ChatColor.GOLD).append("Usage: ").append(ChatColor.WHITE);
            fullText.append(split[0]).append("\n");

            if (cmd.getAliases().size() > 0) {
                fullText.append(ChatColor.BOLD).append(ChatColor.GOLD).append("Aliases: ").append(ChatColor.WHITE);
                boolean first = true;
                for (String alias : cmd.getAliases()) {
                    if (!first) {
                        fullText.append(", ");
                    }
                    fullText.append(alias);
                    first = false;
                }
                fullText.append("\n");
            }
            if (split.length > 1) {
                fullText.append(split[1]);
            }
            this.fullText = fullText.toString();
        }

        @Override
        public boolean canSee(CommandSender player) {
            if (cmd.getPermissions() != null && cmd.getPermissions().length > 0) {
                for (String perm : cmd.getPermissions()) {
                    if (perm != null && player.hasPermission(perm)) {
                        return true;
                    }
                }
                return false;
            }
            return true;
        }

        @Override
        public String getFullText(CommandSender forWho) {
            if (this.fullText == null || this.fullText.length() == 0) {
                return getShortText();
            } else {
                return this.fullText;
            }
        }

        public static class Factory implements HelpTopicFactory<DynamicBukkitCommand> {
            @Override
            public HelpTopic createTopic(DynamicBukkitCommand command) {
                return new DynamicCommandHelpTopic(command);
            }
        }
    }
}
