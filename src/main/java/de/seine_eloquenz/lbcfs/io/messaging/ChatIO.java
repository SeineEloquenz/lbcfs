package de.seine_eloquenz.lbcfs.io.messaging;

import de.seine_eloquenz.lbcfs.Lbcfs;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Class handling message sending for lbcfs
 */
public final class ChatIO {

    private ChatIO() {

    }

    /**
     * Sends a message to a CommandSender
     *
     * @param recipient recipient to send the message to
     * @param message   message to be sent
     * @param prefix    prefix to be put in front
     */
    public static void send(final CommandSender recipient, final String prefix, final String message) {
        recipient.sendMessage(compileMessage(message, prefix));
    }

    /**
     * Sends a message to all team members
     * @param sender sender of the message
     * @param message message to be sent
     */
    public static void sendTeam(final CommandSender sender, final String message) {
        broadcast(sender.getName() + ": " + ChatColor.AQUA + message, "Teamchat", "AscSys.moderator");
    }

    /**
     * Sends message(s) to a CommandSender
     * @param recipient recipient to send the messages to
     * @param messages messages to send
     */
    public static void sendWithoutPrefix(final CommandSender recipient, final String... messages) {
        for (final String message : messages) {
            recipient.sendMessage(message);
        }
    }

    /**
     * Broadcasts a message to all online players
     *
     * @param message message to be sent
     * @param prefix  prefix to be put in front
     */
    public static void broadcast(final String message, final String prefix) {
        Bukkit.broadcastMessage(compileMessage(message, prefix));
    }

    /**
     * Broadcasts a message to all online players with the given permission
     *
     * @param message message to be sent
     * @param prefix  prefix to be put in front
     * @param permission permission to send to
     */
    public static void broadcast(final String message, final String prefix, final String permission) {
        Bukkit.broadcast(compileMessage(message, prefix), permission);
    }

    /**
     * Creates the prefix with color codes
     *
     * @param prefix prefix to add color codes tp
     * @return color coded prefix
     */
    private static String colorCode(final String prefix) {
        return ChatColor.DARK_GRAY + "[" + Lbcfs.getPrimaryColor() + prefix
                + ChatColor.DARK_GRAY + "] " + Lbcfs.getSecondaryColor();
    }

    /**
     * Compiles prefix and message
     *
     * @param message message to be sent
     * @param prefix  prefix to be put in front
     * @return complete message
     */
    private static String compileMessage(final String message, final String prefix) {
        final String finalMessage = colorCode(prefix) + message;
        return finalMessage.trim();
    }
}
