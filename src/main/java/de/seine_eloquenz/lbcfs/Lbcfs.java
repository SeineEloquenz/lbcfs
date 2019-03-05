package de.seine_eloquenz.lbcfs;

import de.seine_eloquenz.lbcfs.commands.CmdLbcfs;
import de.seine_eloquenz.lbcfs.commands.CmdStop;
import de.seine_eloquenz.lbcfs.commands.CmdTC;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.annotation.permission.Permission;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Lbcfs main plugin class
 */
@Plugin(name = "LBCFS", version = "1.0")
@Author("Alexander Linder")
@Permission(name = "lbcfs.admin", desc = "This player acts as an administrator for LBCFS")
@Permission(name = "lbcfs.team", desc = "This player is a member of the server team")
public final class Lbcfs extends LbcfsPlugin {

    private static final String BUNDLE_NAME = "locale/Locale";
    private static final String PRIMARY_COLOR_KEY = "primaryColor";
    private static final String SECONDARY_COLOR_KEY = "secondaryColor";

    private static Lbcfs instance;
    private Locale locale;

    @Override
    public String getChatPrefix() {
        return "LBCFS";
    }

    @Override
    public void setup() {
        instance = this;
        locale = new Locale(this.getConfig().getString("language"));
        addCommand(new CmdLbcfs(this));
        addCommand(new CmdStop(this));
        addCommand(new CmdTC(this));
    }

    /**
     * Gets the locale lbcfs uses
     * @return locale
     */
    protected static Locale getLocale() {
        return instance.locale;
    }

    /**
     * Gets a message defined in lbcfs with the loaded locale
     * @param key key of message
     * @return message with the given key
     */
    public static String getLbcfsMessage(final String key) {
        return ResourceBundle.getBundle(BUNDLE_NAME, instance.locale).getString(key);
    }

    /**
     * Gets the primary colour of lbcfs
     * @return primary colour
     */
    public static ChatColor getPrimaryColor() {
        return ChatColor.valueOf(instance.getConfig().getString(PRIMARY_COLOR_KEY));
    }

    /**
     * Gets the secondary colour of lbcfs
     * @return secondary colour
     */
    public static ChatColor getSecondaryColor() {
        return ChatColor.valueOf(instance.getConfig().getString(SECONDARY_COLOR_KEY));
    }
}
