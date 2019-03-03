package de.seine_eloquenz.lbcfs;

import de.seine_eloquenz.lbcfs.command.LbcfsCommand;
import de.seine_eloquenz.lbcfs.io.messaging.ChatIO;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.ResourceBundle;

/**
 * The LbcfsPlugin class represents a plugin created via the Lbcfs framework. The class implements {@link JavaPlugin}
 * and works the same under the hood.
 *
 * The
 *
 * The {@link LbcfsPlugin#setup()} and {@link LbcfsPlugin#tearDown()} replace the methods {@link JavaPlugin#onEnable()}
 * and {@link JavaPlugin#onDisable()} of normal spigot plugins.
 */
public abstract class LbcfsPlugin extends JavaPlugin {

    private static final String CONFIG_MISSING = "config.yml not found, creating default";
    private static final String CONFIG_FOUND = "config.yml found, loading";

    private static final String BUNDLE_NAME = "locale/Locale";

    private Collection<Listener> listeners;
    private Collection<LbcfsCommand> commands;

    @Override
    public final void onEnable() {
        listeners = new HashSet<>();
        commands = new HashSet<>();
        final File file = new File(this.getDataFolder(), "config.yml");
        if (!file.exists()) {
            this.saveDefaultConfig();
            this.getLogger().info(CONFIG_MISSING);
        } else {
            this.reloadConfig();
            this.getLogger().info(CONFIG_FOUND);
        }
        setup();
        commands.forEach(command -> this.getCommand(command.getName().toLowerCase()).setExecutor(command));
        listeners.forEach(listener -> Bukkit.getServer().getPluginManager().registerEvents(listener, this));

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

    }

    /**
     * Run whent the plugin is disabled
     */
    @SuppressWarnings("EmptyMethod")
    public void tearDown() {

    }

    /**
     * Sends a message to a CommandSender
     *
     * @param recipient recipient to send the message to
     * @param message   message to be sent
     */
    public final void send(final CommandSender recipient, final String message) {
        ChatIO.send(recipient, message, this.getChatPrefix());
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
        ChatIO.broadcast(message, this.getChatPrefix());
    }

    /**
     * Broadcasts the given message to all players on the server with the given permission
     * @param message message to send
     * @param permission permission to send to
     */
    public final void broadcast(final String message, final String permission) {
        ChatIO.broadcast(message, this.getChatPrefix(), permission);
    }

    /**
     * Registers a {@link Listener} for this plugin
     * @param eventListener listener to register
     */
    public final void addListener(final Listener eventListener) {
        listeners.add(eventListener);
    }

    /**
     * Registers a {@link LbcfsCommand} for this plugin
     * @param command command to register
     */
    public final void addCommand(final LbcfsCommand command) {
        commands.add(command);
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
