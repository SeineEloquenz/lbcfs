package de.seine_eloquenz.lbcfs.commands;

import de.seine_eloquenz.lbcfs.Lbcfs;
import de.seine_eloquenz.lbcfs.LbcfsPlugin;
import de.seine_eloquenz.lbcfs.annotations.command.MinArgs;
import de.seine_eloquenz.lbcfs.command.LbcfsCommand;
import de.seine_eloquenz.lbcfs.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.annotation.command.Command;

/**
 * Sends a message to all online team members
 */
@MinArgs(1)
@Command(name = "tc", desc = "Sends a message to all online team members", permission = "lbcfs.team")
public class CmdTC extends LbcfsCommand<Lbcfs> {

    /**
     * Constructs a new {@link CmdTC} object
     * @param plugin the plugin the command belongs to
     */
    public CmdTC(final Lbcfs plugin) {
        super(plugin);
    }

    @Override
    public boolean run(final CommandSender sender, final String[] args) {
        this.getPlugin().sendTeam(sender, StringUtils.createMessageFromArgs(args));
        return true;
    }
}
