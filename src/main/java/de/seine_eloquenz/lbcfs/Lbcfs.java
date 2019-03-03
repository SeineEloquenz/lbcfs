package de.seine_eloquenz.lbcfs;

import org.bukkit.ChatColor;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Lbcfs main plugin class
 */
public final class Lbcfs extends LbcfsPlugin {


    private static final String BUNDLE_NAME = "locale/Locale";
    private static final String PRIMARY_COLOR_KEY = "primaryColour";
    private static final String SECONDARY_COLOR_KEY = "secondaryColour";

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
