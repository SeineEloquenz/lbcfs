package de.seine_eloquenz.lbcfs.commands;

import de.seine_eloquenz.annotation.command.Command;
import de.seine_eloquenz.lbcfs.Lbcfs;
import de.seine_eloquenz.lbcfs.annotations.command.MinArgs;
import de.seine_eloquenz.lbcfs.command.LbcfsCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.logging.Level;

/**
 * Debug command to log a string to console
 */
@MinArgs(0)
@Command(name = "log", desc = "Logs a message to console", permission = "lbcfs.admin", usage = "/log <message to log>")
public class CmdLog extends LbcfsCommand<Lbcfs> {

    /**
     * Constructs a new {@link CmdLog} object
     * @param plugin plugin the command belongs to
     */
    public CmdLog(final Lbcfs plugin) {
        super(plugin);
    }

    @Override
    public boolean run(final CommandSender sender, final String[] args) {
        String msg = String.join(" ", args);
        Bukkit.getLogger().log(Level.INFO, msg);
        sender.sendMessage("Logged message: " + msg);
        return true;
    }
}
