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
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * LbcfsCommand is the class you will have to overwrite to create a command with lbcfs
 * @param <T> your plugin
 */
public abstract class LbcfsCommand <T extends LbcfsPlugin> implements CommandExecutor, TabCompleter {

    private final LbcfsPlugin plugin;
    private final Class<T> pluginClass;
    private final Map<String, SubCommand> subCommands;
    private final int minParams;
    private final int maxParams;
    private final TOpt[] tabOptions;

    /**
     * Creates a new LbcfsCommand for the given plugin
     * Has to be annotated with {@link org.bukkit.plugin.java.annotation.command.Command}
     * You may <b>not</b> annotate commands in your {@link LbcfsPlugin}s main class!
     * Name may not be "?" as this is reserved
     *
     * @param plugin the plugin this command belongs to
     */
    public LbcfsCommand(final T plugin) {
        if ("?".equals(this.getName())) {
            throw new IllegalArgumentException("Invalid name '" + this.getName() + "'!");
        }
        this.plugin = plugin;
        this.pluginClass = (Class<T>) plugin.getClass();
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
        this.tabOptions = constructTabList(getTabOptions());
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
                        final Constructor<?> subCmdConstructor = this.getSubCommandConstructor(subCmd);
                        this.validateSubCmdConstructor(subCmdConstructor, subCmd);
                        subCommand = (SubCommand) subCmdConstructor.newInstance(plugin);
                    } else { //subcommand is inner class
                        if (Modifier.isStatic(subCmd.getModifiers())) {
                            final Constructor<?> subCmdConstructor = this.getSubCommandConstructor(subCmd);
                            this.validateSubCmdConstructor(subCmdConstructor, subCmd);
                            subCommand = (SubCommand) subCmdConstructor.newInstance(plugin);
                        } else {
                            final Constructor<?> subCmdConstructor = Stream.of(subCmd.getConstructors())
                                    .filter(c -> this.getClass().isAssignableFrom(c.getParameterTypes()[0])
                                            && LbcfsPlugin.class.isAssignableFrom(c.getParameterTypes()[1]))
                                    .findFirst().orElse(null);
                            this.validateSubCmdConstructor(subCmdConstructor, subCmd);
                            subCommand = (SubCommand) subCmdConstructor.newInstance(this, plugin);
                        }
                    }
                    subCommands.put(subCommand.getName(), subCommand);
                } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace(); //Should never happen in production, as all commands need to supply this constructor
                }
            }
        }
    }

    private Constructor<?> getSubCommandConstructor(final Class<?> subCmd) {
        return Stream.of(subCmd.getConstructors())
                .filter(c -> LbcfsPlugin.class.isAssignableFrom(c.getParameterTypes()[0]))
                .findFirst().orElse(null);
    }

    private void validateSubCmdConstructor(Constructor<?> subCmdConstructor, Class<?> subCmd) throws InstantiationException {
        if (subCmdConstructor == null) {
            throw new InstantiationException("Could not find Constructor of " + subCmd.getName()
                    + " of declaring command " + this.getName() + " of declaring plugin "
                    + this.getName() + " which takes only the plugin as argument!");
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
    public final boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command,
                                   @NotNull final String label, final String[] args) {
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
        return this.getClass().getAnnotation(de.seine_eloquenz.annotation.command.Command.class).name();
    }

    /**
     * Gets the plugin this command is associated with
     * @return plugin
     */
    public final T getPlugin() {
        return pluginClass.cast(plugin);
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

    /**
     * Executes when tab is used on the command.
     * <br/>
     * <b>Override only if you know what you do and don't want to use {@link Lbcfs} default behaviour</b>
     * @param sender - the sender that executes the command
     * @param alias - the command alias used for this action
     * @param args - the args of the command
     * @return args to use for tab completion
     */
    @Override
    public List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull Command command,
                                            @NotNull final String alias, @NotNull final String[] args) {
        if (args.length == 1) {
            Stream<String> tabOptionStream;
            if (tabOptions.length < 1) {
                tabOptionStream = Stream.empty();
            } else {
                tabOptionStream = Arrays.stream(tabOptions[0].get((Player) sender));
            }
            return Stream.concat(subCommands.values().stream().map(SubCommand::getName),
                    tabOptionStream).collect(Collectors.toList());
        }
        final SubCommand subCmd = subCommands.get(args.length > 1 ? args[0] : null);
        if (subCmd != null) {
            //noinspection unchecked
            return subCmd.onTabComplete(sender, command, alias, cutFirstParam(args));
        } else {
            if (args.length > tabOptions.length) {
                return new ArrayList<>();
            }
            return Arrays.stream(tabOptions[args.length - 1].get((Player) sender))
                    .filter(o -> o.startsWith(args[args.length - 1])).collect(Collectors.toList());
        }
    }

    /**
     * Constructs the tab complete options from the entered values. You may supply as many options as you like
     * The tab complete options have to be sorted in their natural order
     * @param tabOptions options for this command
     * @return constructed tablist mapping
     */
    private TOpt[] constructTabList(TOpt[] tabOptions) {
        if (tabOptions == null) {
            return new TOpt[0];
        }
        if (tabOptions.length > maxParams) {
            this.plugin.getLogger().log(Level.WARNING, "More tab options were provided for " + this.getName()
            + " of plugin " + this.plugin.getName() + "! Options were truncated at max!");
        }
        return Arrays.copyOfRange(tabOptions, 0, tabOptions.length);
    }

    /**
     * Override this method to supply tab options to your command. Supply an array of options per allowed param depth
     *
     * Example: maxParams = 2
     * new String[][]{ { "test", "version"}, { help } }
     * @return tab options
     */
    protected TOpt[] getTabOptions() {
        return null;
    }

    private boolean isPlayerOnly() {
        return this.getClass().isAnnotationPresent(PlayerOnly.class);
    }
}
