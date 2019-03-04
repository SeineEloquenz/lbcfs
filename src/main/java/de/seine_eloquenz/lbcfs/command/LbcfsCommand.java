package de.seine_eloquenz.lbcfs.command;

import de.seine_eloquenz.lbcfs.Lbcfs;
import de.seine_eloquenz.lbcfs.LbcfsPlugin;
import de.seine_eloquenz.lbcfs.annotations.command.PlayerOnly;
import de.seine_eloquenz.lbcfs.io.messaging.ChatIO;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * LbcfsCommand is the class you will have to overwrite to create a command with lbcfs
 */
public abstract class LbcfsCommand implements CommandExecutor {

    private final String name;
    private final LbcfsPlugin plugin;
    private final Map<String, SubCommand> subCommands;
    private final int minParams;
    private final int maxParams;

    /**
     * Creates a new LbcfsCommand without parameters for the given plugin
     * Name may not be "?" as this is reserved
     *
     * @param name name of the command
     * @param plugin the plugin this command belongs to
     */
    public LbcfsCommand(final String name, final LbcfsPlugin plugin) {
        this(name, plugin, 0, 0);
    }

    /**
     * Creates a new LbcfsCommand for the given plugin
     * Name may not be "?" as this is reserved
     *
     * @param name name of the command
     * @param plugin the plugin this command belongs to
     * @param minParams minimal number of command parameters
     * @param maxParams maximal number of command parameters
     */
    public LbcfsCommand(final String name, final LbcfsPlugin plugin, final int minParams, final int maxParams) {
        if ("?".equals(name)) {
            throw new IllegalArgumentException("Invalid name '" + name + "'!");
        }
        this.name = name;
        this.plugin = plugin;
        subCommands = new HashMap<>();
        this.minParams = minParams;
        this.maxParams = maxParams;
    }

    private String[] cutFirstParam(final String[] params) {
        final String[] subParams = new String[params.length - 1];
        if (subParams.length >= 0) {
            System.arraycopy(params, 1, subParams, 0, subParams.length);
        }
        return subParams;
    }

    @Override
    public final boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length == 0) {
            return runLogic(sender, args);
        }
        if (args[0].equals("?")) {
            return listSubcommands(sender);
        }
        return subCommands.get(args[0]).onCommand(sender, command, label, cutFirstParam(args));
    }

    /**
     * Gets the name of the command
     * @return name of the command
     */
    public final String getName() {
        return name;
    }

    /**
     * Gets the plugin this command is associated with
     * @return plugin
     */
    public final LbcfsPlugin getPlugin() {
        return plugin;
    }

    /**
     * Adds a {@link SubCommand} to this command
     * @param subCommand subcommand to add
     */
    public final void addSubCommand(final SubCommand subCommand) {
        subCommands.put(subCommand.getName(), subCommand);
    }

    /**
     * Runs the command logic
     *
     * @param sender sender who executed the command
     * @param params parameters given to the command
     * @return true if execution was completed without errors, false otherwise
     */
    private boolean runLogic(final CommandSender sender, final String[] params) {
        if (isPlayerOnly() && !(sender instanceof Player)) {
            sender.sendMessage(Lbcfs.getLbcfsMessage("playersOnly"));
            return true;
        }
        if (validateParameterCount(params)) {
            return run(sender, params);
        } else {
            if (minParams == 0 && maxParams == 0) {
                plugin.send(sender, Lbcfs.getLbcfsMessage("commandNoParams"));
                return true;
            }
            if (minParams > 0 && maxParams == 0) {
                plugin.send(sender, String.format(Lbcfs.getLbcfsMessage("commandAtLeastParams"), minParams));
                return true;
            }
            if (minParams == 0 && maxParams > 0) {
                plugin.send(sender, String.format(Lbcfs.getLbcfsMessage("commandAtMostParams"), maxParams));
                return true;
            }
            if (minParams > 0 && maxParams > 0) {
                plugin.send(sender, String.format(Lbcfs.getLbcfsMessage("commandParams"), minParams, maxParams));
                return true;
            } else {
                return true;
            }
        }
    }

    private boolean validateParameterCount(final String[] params) {
        return (params.length >= minParams && params.length <= maxParams)
                || (minParams == -1 && params.length <= maxParams)
                || (params.length >= minParams && maxParams == -1)
                || (minParams == -1 && maxParams == -1);
    }

    /**
     * Runs this command for the sender with the given arguments
     * @param sender sender who issued the command
     * @param args arguments passed to the command
     * @return true if execution was completed without errors, false otherwise
     */
    public abstract boolean run(CommandSender sender, String[] args);

    private boolean listSubcommands(final CommandSender sender) {
        ChatIO.sendWithoutPrefix(sender, Lbcfs.getPrimaryColor() + Lbcfs.getLbcfsMessage("subcommands"));
        for (final String name : subCommands.keySet()) {
            ChatIO.sendWithoutPrefix(sender, Lbcfs.getPrimaryColor() + name);
        }
        return true;
    }

    private boolean isPlayerOnly() {
        return this.getClass().isAnnotationPresent(PlayerOnly.class);
    }
}
