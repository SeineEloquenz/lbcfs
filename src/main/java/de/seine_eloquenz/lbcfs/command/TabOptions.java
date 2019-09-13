package de.seine_eloquenz.lbcfs.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public final class TabOptions {

    private TabOptions() {
        //Util classes should not be instantiated
    }

    public static TabOption of(String... elements) {
        return new TabOption() {
            @Override
            public String[] of(final String... elements) {
                return elements;
            }
        };
    }

    public static TabOption getPlayers() {
        return elements -> Bukkit.getOnlinePlayers().stream().map(Player::getName).toArray(String[]::new);
    }
}
