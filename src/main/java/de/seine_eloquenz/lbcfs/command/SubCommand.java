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
        this(name, plugin, 0, 0);
    }

    /**
     * Creates a new SubCommand with the given minimal and maximal parameters
     * NOTE: Name "?" is reserved for listing of subcommands
     *
     * @param name name of the subcommand
     * @param plugin plugin this command belongs to
     * @param minParams minimum parameter count
     * @param maxParams maximum parameter count
     */
    public SubCommand(final String name, final LbcfsPlugin plugin, final int minParams, final int maxParams) {
        super(name, plugin, minParams, maxParams);
    }

    @Override
    public abstract boolean run(CommandSender sender, String[] args);
}