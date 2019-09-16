package de.seine_eloquenz.lbcfs.command;

import org.bukkit.entity.Player;

/**
 * Interface providing a method which returns a String array used in tab completion. Various pre-defined TOpts can be
 * obtained from {@link TOpts}
 */
@FunctionalInterface
public interface TOpt {

    /**
     * Gets the strings the {@link TOpt} represents
     * @param player Player who issued the command
     * @return possibly empty string array
     */
    String[] get(Player player);
}
