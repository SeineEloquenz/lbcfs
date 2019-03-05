package de.seine_eloquenz.lbcfs.commands;

import de.seine_eloquenz.lbcfs.LbcfsPlugin;
import de.seine_eloquenz.lbcfs.annotations.command.SubCommand;
import de.seine_eloquenz.lbcfs.annotations.command.SubCommandName;
import de.seine_eloquenz.lbcfs.command.LbcfsCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.annotation.command.Command;

/**
 * Prints the version of Lbcfs in chat
 */
@Command(name = "lbcfs", desc = "Lbcfs main command", permission = "lbcfs.admin")
@SubCommand(CmdLbcfs.Version.class)
public class CmdLbcfs extends LbcfsCommand {

    /**
     * Constructs a new {@link CmdLbcfs} object
     * @param plugin plugin the command belongs to
     */
    public CmdLbcfs(final LbcfsPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean run(final CommandSender sender, final String[] args) {
        return false;
    }

    /**
     * This subcommand prints the version of lbcfs into chat
     */
    @SubCommandName("version")
    public final class Version extends de.seine_eloquenz.lbcfs.command.SubCommand {

        /**
         * Creates a new SubCommand with the given minimal and maximal parameters
         * NOTE: Name "?" is reserved for listing of subcommands
         *
         * @param plugin plugin this command belongs to
         */
        public Version(final LbcfsPlugin plugin) {
            super(plugin);
        }

        @Override
        public boolean run(final CommandSender sender, final String[] args) {
            this.getPlugin().send(sender, this.getPlugin().getMessage("version") + ": " + this.getPlugin().getDescription().getVersion());
            return true;
        }
    }
}
