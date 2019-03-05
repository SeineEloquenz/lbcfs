package de.seine_eloquenz.lbcfs.commands;

import de.seine_eloquenz.lbcfs.LbcfsPlugin;
import de.seine_eloquenz.lbcfs.annotations.command.SubCommandName;
import de.seine_eloquenz.lbcfs.command.SubCommand;
import org.bukkit.command.CommandSender;

/**
 * This subcommand prints the version of lbcfs into chat
 */
@SubCommandName("version")
public class SubCmdVersion extends SubCommand {

    /**
     * Creates a new SubCommand with the given minimal and maximal parameters
     * NOTE: Name "?" is reserved for listing of subcommands
     *
     * @param plugin plugin this command belongs to
     */
    public SubCmdVersion(final LbcfsPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean run(final CommandSender sender, final String[] args) {
        this.getPlugin().send(sender, this.getPlugin().getMessage("version") + ": " + this.getPlugin().getDescription().getVersion());
        return true;
    }
}
