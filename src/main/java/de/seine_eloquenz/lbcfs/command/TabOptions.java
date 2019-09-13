package de.seine_eloquenz.lbcfs.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public final class TabOptions {

    private TabOptions() {
        //Util classes should not be instantiated
    }

    public static TabOption of(String... elements) {
        return new Elements(elements);
    }

    public static TabOption getPlayers() {
        return () -> Bukkit.getOnlinePlayers().stream().map(Player::getName).toArray(String[]::new);
    }

    private static final class Elements implements TabOption {

        private final String[] elements;

        public Elements(String... elements) {
            this.elements = elements;
        }

        @Override
        public String[] get() {
            return elements;
        }
    }
}
