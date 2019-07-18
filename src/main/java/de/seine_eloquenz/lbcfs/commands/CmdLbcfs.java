package de.seine_eloquenz.lbcfs.commands;

import de.seine_eloquenz.lbcfs.Lbcfs;
import de.seine_eloquenz.lbcfs.LbcfsPlugin;
import de.seine_eloquenz.lbcfs.annotations.command.SubCommand;
import de.seine_eloquenz.lbcfs.command.LbcfsCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.annotation.command.Command;

/**
 * Prints the version of Lbcfs in chat
 */
@Command(name = "lbcfs", desc = "Lbcfs main command", permission = "lbcfs.admin")
public class CmdLbcfs extends LbcfsCommand<Lbcfs> {

    /**
     * Constructs a new {@link CmdLbcfs} object
     * @param plugin plugin the command belongs to
     */
    public CmdLbcfs(final Lbcfs plugin) {
        super(plugin);
    }

    @Override
    public boolean run(final CommandSender sender, final String[] args) {
        return false;
    }

    /**
     * This subcommand prints the version of lbcfs into chat
     */
    @SubCommand(name = "version", parentCommand = CmdLbcfs.class)
    public final class Version extends de.seine_eloquenz.lbcfs.command.SubCommand<Lbcfs> {

        /**
         * Creates a new SubCommand with the given minimal and maximal parameters
         * NOTE: Name "?" is reserved for listing of subcommands
         *
         * @param plugin plugin this command belongs to
         */
        public Version(final Lbcfs plugin) {
            super(plugin);
        }

        @Override
        public boolean run(final CommandSender sender, final String[] args) {
            this.getPlugin().send(sender, this.getPlugin().getMessage("version") + ": " + this.getPlugin().getDescription().getVersion());
            return true;
        }
    }
}
