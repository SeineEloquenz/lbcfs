package de.seine_eloquenz.lbcfs.command;

import de.seine_eloquenz.lbcfs.LbcfsPlugin;
import org.bukkit.command.CommandSender;

/**
 * AscCommand
 */
public abstract class SubCommand extends LbcfsCommand {

    /**
     * Creates a new SubCommand with no parameters which can be issued by every {@link CommandSender}
     * NOTE: Name "?" is reserved for listing of subcommands
     *
     * @param name name of the subcommand
     * @param plugin plugin this command belongs to
     */
    public SubCommand(final String name, final LbcfsPlugin plugin) {
        this(name, plugin, 0, 0, false);
    }

    /**
     * Creates a new SubCommand with the given minimal and maximal parameters by every {@link CommandSender}
     * NOTE: Name "?" is reserved for listing of subcommands
     *
     * @param name name of the subcommand
     * @param plugin plugin this command belongs to
     * @param minParams minimum parameter count
     * @param maxParams maximum parameter count
     */
    public SubCommand(final String name, final LbcfsPlugin plugin, final int minParams, final int maxParams) {
        this(name, plugin, minParams, maxParams, false);
    }

    /**
     * Creates a new SubCommand with the given minimal and maximal parameters
     * NOTE: Name "?" is reserved for listing of subcommands
     *
     * @param name name of the subcommand
     * @param plugin plugin this command belongs to
     * @param minParams minimum parameter count
     * @param maxParams maximum parameter count
     * @param playerOnly whether only players can execute the command or not
     */
    public SubCommand(final String name, final LbcfsPlugin plugin, final int minParams, final int maxParams, final boolean playerOnly) {
        super(name, plugin, minParams, maxParams, playerOnly);
    }

    @Override
    public abstract boolean run(CommandSender sender, String[] args);
}