package de.seine_eloquenz.lbcfs.commands;

import de.seine_eloquenz.lbcfs.LbcfsPlugin;
import de.seine_eloquenz.lbcfs.annotations.command.SubCommand;
import de.seine_eloquenz.lbcfs.command.LbcfsCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.annotation.command.Command;

/**
 * Prints the version of Lbcfs in chat
 */
@Command(name = "lbcfs")
@SubCommand(SubCmdVersion.class)
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
}
