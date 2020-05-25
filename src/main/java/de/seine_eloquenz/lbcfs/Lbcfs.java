package de.seine_eloquenz.lbcfs;

import de.seine_eloquenz.annotation.dependency.Dependency;
import de.seine_eloquenz.annotation.permission.Permission;
import de.seine_eloquenz.annotation.plugin.ApiVersion;
import org.bukkit.ChatColor;
import de.seine_eloquenz.annotation.plugin.Plugin;
import de.seine_eloquenz.annotation.plugin.author.Author;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Lbcfs main plugin class
 */
@Plugin(name = "LBCFS", version = "1.0")
@ApiVersion(ApiVersion.Target.v1_15)
@Author("Alexander Linder")
@Dependency("Lbcfs-Plugin-Annotations")
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
        String loc = this.getConfig().getString("language");
        if (loc == null) {
            locale = Locale.ENGLISH;
        } else {
            locale = new Locale(loc);
        }
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
