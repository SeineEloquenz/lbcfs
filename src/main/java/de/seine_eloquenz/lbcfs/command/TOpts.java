package de.seine_eloquenz.lbcfs.command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * Utility class providing factory methods for various possible {@link TOpt} objects
 */
public final class TOpts {

    private TOpts() {
        //Util classes should not be instantiated
    }

    /**
     * Creates a {@link TOpt} from the given strings
     * @param elements strings to use
     * @return TOpt of the strings
     */
    public static TOpt of(String... elements) {
        return new Elements(elements);
    }

    /**
     * Creates a {@link TOpt} which returns all online Players
     * @return TOpt of all online players
     */
    public static TOpt getPlayers() {
        return () -> Bukkit.getOnlinePlayers().stream().map(Player::getName).toArray(String[]::new);
    }

    /**
     * Creates a {@link TOpt} which returns all {@link Material}s
     * @return material array
     */
    public static TOpt getMaterials() {
        return () -> Arrays.stream(Material.values()).map(Material::name).toArray(String[]::new);
    }

    private static final class Elements implements TOpt {

        private final String[] elements;

        private Elements(String... elements) {
            this.elements = elements;
        }

        @Override
        public String[] get() {
            return elements;
        }
    }
}
