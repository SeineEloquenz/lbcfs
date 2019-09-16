package de.seine_eloquenz.lbcfs.command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

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
        return p -> Bukkit.getOnlinePlayers().stream().map(Player::getName).toArray(String[]::new);
    }

    /**
     * Creates a {@link TOpt} which returns all {@link Material}s
     * @return material array
     */
    public static TOpt getMaterials() {
        return p -> Arrays.stream(Material.values()).map(Material::name).toArray(String[]::new);
    }

    /**
     * Returns either the x coordinate of the block the player is looking at or a '~' character for players location if
     * not
     * @return coord representation
     */
    public static TOpt coordX() {
        return p -> getCoordOption(p, 'x');
    }

    /**
     * Returns either the y coordinate of the block the player is looking at or a '~' character for players location if
     * not
     * @return coord representation
     */
    public static TOpt coordY() {
        return p -> getCoordOption(p, 'y');
    }

    /**
     * Returns either the z coordinate of the block the player is looking at or a '~' character for players location if
     * not
     * @return coord representation
     */
    public static TOpt coordZ() {
        return p -> getCoordOption(p, 'z');
    }

    private static String[] getCoordOption(Player player, char axis) throws IllegalArgumentException {
        final RayTraceResult rayTraceResult = player.rayTraceBlocks(5);
        if (rayTraceResult == null) {
            return new String[]{"~"};
        } else {
            switch (axis) {
                case 'x':
                    return new String[]{"" + rayTraceResult.getHitPosition().getBlockX()};
                case 'y':
                    return new String[]{"" + rayTraceResult.getHitPosition().getBlockY()};
                case 'z':
                    return new String[]{"" + rayTraceResult.getHitPosition().getBlockZ()};
                default:
                    throw new IllegalArgumentException("Wrong direction " + axis + " given!");
            }
        }
    }

    private static final class Elements implements TOpt {

        private final String[] elements;

        private Elements(String... elements) {
            this.elements = elements;
        }

        @Override
        public String[] get(Player player) {
            return elements;
        }
    }
}
