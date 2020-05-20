package de.seine_eloquenz.lbcfs;

import de.seine_eloquenz.lbcfs.command.LbcfsCommand;
import de.seine_eloquenz.lbcfs.io.messaging.ChatIO;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Stream;

/**
 * The LbcfsPlugin class represents a plugin created via the Lbcfs framework. The class implements {@link JavaPlugin}
 * and works the same under the hood.
 *
 * The {@link LbcfsPlugin#setup()} and {@link LbcfsPlugin#tearDown()} replace the methods {@link JavaPlugin#onEnable()}
 * and {@link JavaPlugin#onDisable()} of normal spigot plugins.
 */
public abstract class LbcfsPlugin extends JavaPlugin {

    private static final String CONFIG_MISSING = "config.yml not found, creating default";
    private static final String CONFIG_FOUND = "config.yml found, loading";
    private static final String NO_CONFIG = "Plugin has no configuration.";

    private static final String BUNDLE_NAME = "locale/Locale";

    private Collection<Listener> listeners;
    private Collection<LbcfsCommand> commands;
    private Map<Class<Plugin>, Plugin> hardDependencies;
    private Map<Class<Plugin>, Plugin> softDependencies;

    @Override
    public final void onEnable() {
        listeners = new HashSet<>();
        commands = new HashSet<>();
        hardDependencies = new HashMap<>();
        softDependencies = new HashMap<>();
        final File file = new File(this.getDataFolder(), "config.yml");
        if (!file.exists()) {
            if (this.getResource("config.yml") != null) {
                this.saveDefaultConfig();
                this.getLogger().info(CONFIG_MISSING);
            } else {
                this.getLogger().info(NO_CONFIG);
            }
        } else {
            this.reloadConfig();
            this.getLogger().info(CONFIG_FOUND);
        }
        this.registerDependencies();
        this.findAndRegisterCommands();
        this.findAndRegisterListeners();
        setup();
        listeners.forEach(listener -> Bukkit.getServer().getPluginManager().registerEvents(listener, this));

    }

    private void registerDependencies() {
        this.getDescription().getDepend().forEach(dependency -> registerDependency(dependency, hardDependencies));
        this.getDescription().getSoftDepend().forEach(dependency -> registerDependency(dependency, softDependencies));
    }

    private void registerDependency(String pluginName, Map<Class<Plugin>, Plugin> dependencyMap) {
        final Plugin depPlugin = Bukkit.getPluginManager().getPlugin(pluginName);
        assert depPlugin != null;
        dependencyMap.put((Class<Plugin>) depPlugin.getClass(), depPlugin);
    }

    /**
     * Gets a registered hard dependency
     * @param dependencyClass the class of the plugin to get
     * @param <T> Type of the dependency class to get
     * @return the main plugin instance of the dependency
     */
    //hard dependencies can't get null, as the dependent plugin won't even be loaded if they are missing, so no null return here
    @NotNull
    public final <T extends JavaPlugin> T getHardDependency(final Class<T> dependencyClass) {
        return dependencyClass.cast(this.hardDependencies.get(dependencyClass));
    }

    /**
     * Gets a registered soft dependency
     * @param dependencyClass the class of the plugin to get
     * @param <T> Type of the dependency class to get
     * @return the main plugin instance of the dependency, null if the soft dependency was not found
     */
    public final <T extends JavaPlugin> T getSoftDependency(final Class<T> dependencyClass) {
        return dependencyClass.cast(this.softDependencies.get(dependencyClass));
    }

    private void findAndRegisterCommands() {
        final Reflections reflections = new Reflections(this.getClass().getPackageName());
        final Set<Class<?>> cmdClasses = reflections.getTypesAnnotatedWith(Command.class);
        for (final Class<?> cmd : cmdClasses) {
            try {
                final Constructor<?> constructor =
                        Stream.of(cmd.getConstructors())
                                .filter(c -> LbcfsPlugin.class.isAssignableFrom(c.getParameterTypes()[0]))
                                .findFirst().orElse(null);
                if (constructor == null) {
                    throw new InstantiationException("Could not find Constructor of " + cmd.getName()
                    + " of declaring plugin " + this.getName() + " which takes only the plugin as argument!");
                }
                final LbcfsCommand command = (LbcfsCommand) constructor.newInstance(this);
                commands.add(command);
            } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace(); //Should never happen in production, as all commands need to supply this constructor
            }
        }
        commands.forEach(command -> this.getCommand(command.getName().toLowerCase()).setExecutor(command));
    }

    private void  findAndRegisterListeners() {
        final Reflections reflections = new Reflections(this.getClass().getPackageName());
        final Set<Class<?>> listenerClasses = reflections.getTypesAnnotatedWith(de.seine_eloquenz.lbcfs.annotations.Listener.class);
        for (final Class<?> listenerClass : listenerClasses) {
            try {
                final Constructor<?> constructor = listenerClass.getConstructor();
                final Listener listener = (Listener) constructor.newInstance();
                listeners.add(listener);
            } catch (final NoSuchMethodException e) {
                // We skip this Listener, as it doesn't have the needed default constructor
            } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace(); //Should never happen in production, as all commands need to supply this constructor
            }
        }
    }

    @Override
    public final void onDisable() {
        tearDown();
    }

    /**
     * This method returns the prefix the plugin will use in chat
     * @return the chatPrefix
     */
    public abstract String getChatPrefix();

    /**
     * Run when the plugin is enabled
     */
    @SuppressWarnings("EmptyMethod")
    public void setup() {
        //This method can but doesn't have to be overwritten by inheriting classes of LbcfsPlugin
    }

    /**
     * Run when the plugin is disabled
     */
    @SuppressWarnings("EmptyMethod")
    public void tearDown() {
        //This method can but doesn't have to be overwritten by inheriting classes of LbcfsPlugin
    }

    /**
     * Sends a message to a CommandSender
     *
     * @param recipient recipient to send the message to
     * @param message   message to be sent
     */
    public final void send(final CommandSender recipient, final String message) {
        ChatIO.send(recipient, this.getChatPrefix(), message);
    }


    /**
     * Sends a message to all team members
     * @param sender sender of the message
     * @param message message to be sent
     */
    public final void sendTeam(final CommandSender sender, final String message) {
        ChatIO.sendTeam(sender, message);
    }

    /**
     * Sends a message to a CommandSender without a prefix
     *
     * @param recipient recipient to send the message to
     * @param message   message to be sent
     */
    public final void sendWithoutPrefix(final CommandSender recipient, final String message) {
        ChatIO.sendWithoutPrefix(recipient, message);
    }

    /**
     * Sends messages to a CommandSender
     * @param recipient recipient to send the messages to
     * @param messages messages to send
     */
    public final void sendWithoutPrefix(final CommandSender recipient, final String... messages) {
        ChatIO.sendWithoutPrefix(recipient, messages);
    }

    /**
     * Broadcasts the given message to all players on the server
     * @param message message to send
     */
    public final void broadcast(final String message) {
        ChatIO.broadcast(this.getChatPrefix(), message);
    }

    /**
     * Broadcasts the given message to all players on the server with the given permission
     * @param message message to send
     * @param permission permission to send to
     */
    public final void broadcast(final String message, final String permission) {
        ChatIO.broadcast(this.getChatPrefix(), message, permission);
    }

    /**
     * Registers a {@link Listener} for this plugin
     * @param eventListener listener to register
     */
    public final void addListener(final Listener eventListener) {
        listeners.add(eventListener);
    }

    /**
     * Gets a localised string with the language specified in the lbcfs config
     * @param key key to get
     * @return localised string
     */
    public final String getMessage(final String key) {
        return ResourceBundle.getBundle(BUNDLE_NAME, Lbcfs.getLocale()).getString(key);
    }
}
