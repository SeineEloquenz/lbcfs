package de.seine_eloquenz.lbcfs.command;

import de.seine_eloquenz.lbcfs.LbcfsPlugin;
import org.bukkit.command.CommandSender;

/**
 * AscCommand
 * @param <T> your plugin type
 */
public abstract class SubCommand <T extends LbcfsPlugin> extends LbcfsCommand<T> {

    /**
     * Creates a new SubCommand with the given minimal and maximal parameters
     * NOTE: Name "?" is reserved for listing of subcommands
     *
     * @param plugin plugin this command belongs to
     */
    public SubCommand(final T plugin) {
        super(plugin);
    }

    @Override
    public abstract boolean run(CommandSender sender, String[] args);

    @Override
    public String getName() {
        return this.getClass().getAnnotation(de.seine_eloquenz.lbcfs.annotations.command.SubCommand.class).name();
    }
}