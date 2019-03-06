package de.seine_eloquenz.lbcfs.command;

import de.seine_eloquenz.lbcfs.Lbcfs;
import de.seine_eloquenz.lbcfs.LbcfsPlugin;
import de.seine_eloquenz.lbcfs.annotations.command.MaxArgs;
import de.seine_eloquenz.lbcfs.annotations.command.MinArgs;
import de.seine_eloquenz.lbcfs.annotations.command.PlayerOnly;
import de.seine_eloquenz.lbcfs.io.messaging.ChatIO;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * LbcfsCommand is the class you will have to overwrite to create a command with lbcfs
 */
public abstract class LbcfsCommand implements CommandExecutor {

    private final LbcfsPlugin plugin;
    private final Map<String, SubCommand> subCommands;
    private final int minParams;
    private final int maxParams;

    /**
     * Creates a new LbcfsCommand for the given plugin
     * Has to be annotated with {@link org.bukkit.plugin.java.annotation.command.Command}
     * You may <b>not</b> annotate commands in your {@link LbcfsPlugin}s main class!
     * Name may not be "?" as this is reserved
     *
     * @param plugin the plugin this command belongs to
     */
    public LbcfsCommand(final LbcfsPlugin plugin) {
        if ("?".equals(this.getName())) {
            throw new IllegalArgumentException("Invalid name '" + this.getName() + "'!");
        }
        this.plugin = plugin;
        subCommands = new HashMap<>();
        if (this.getClass().isAnnotationPresent(MinArgs.class)) {
            this.minParams = this.getClass().getAnnotation(MinArgs.class).value();
        } else {
            this.minParams = -1;
        }
        if (this.getClass().isAnnotationPresent(MaxArgs.class)) {
            this.maxParams = this.getClass().getAnnotation(MaxArgs.class).value();
        } else {
            this.maxParams = -1;
        }
        this.findAndRegisterSubCommands();
    }

    private void findAndRegisterSubCommands() {
        final Reflections reflections = new Reflections(plugin.getClass().getPackageName());
        final Set<Class<?>> subCmdClasses = reflections.getTypesAnnotatedWith(de.seine_eloquenz.lbcfs.annotations.command.SubCommand.class);
        for (final Class<?> subCmd : subCmdClasses) {
            if (subCmd.getAnnotation(de.seine_eloquenz.lbcfs.annotations.command.SubCommand.class).parentCommand()
                    .equals(this.getClass())) {
                try {
                    final SubCommand subCommand;
                    if (subCmd.getEnclosingClass().equals(subCmd)) { //Check if subcommand is declared as own class
                        final Constructor<?> subCmdConstructor = subCmd.getConstructor(LbcfsPlugin.class);
                        subCommand = (SubCommand) subCmdConstructor.newInstance(plugin);
                    } else { //subcommand is inner class
                        if (Modifier.isStatic(subCmd.getModifiers())) {
                            final Constructor<?> subCmdConstructor = subCmd.getConstructor(LbcfsPlugin.class);
                            subCommand = (SubCommand) subCmdConstructor.newInstance(plugin);
                        } else {
                            final Constructor<?> subCmdConstructor = subCmd.getConstructor(this.getClass(), LbcfsPlugin.class);
                            subCommand = (SubCommand) subCmdConstructor.newInstance(this, plugin);
                        }
                    }
                    subCommands.put(subCommand.getName(), subCommand);
                } catch (final NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace(); //Should never happen in production, as all commands need to supply this constructor
                }
            }
        }
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
        final SubCommand subCmd = subCommands.get(args.length > 0 ? args[0] : null);
        if (subCmd != null) {
            return subCmd.onCommand(sender, command, label, cutFirstParam(args));
        } else {
            if (args.length > 0 && args[0].equals("?")) {
                return listSubcommands(sender);
            } else {
                return runLogic(sender, args);
            }
        }
    }

    /**
     * Gets the name of the command
     * @return name of the command
     */
    public String getName() {
        return this.getClass().getAnnotation(org.bukkit.plugin.java.annotation.command.Command.class).name();
    }

    /**
     * Gets the plugin this command is associated with
     * @return plugin
     */
    public final LbcfsPlugin getPlugin() {
        return plugin;
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
