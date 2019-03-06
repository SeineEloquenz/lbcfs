package de.seine_eloquenz.lbcfs.commands;

import de.seine_eloquenz.lbcfs.LbcfsPlugin;
import de.seine_eloquenz.lbcfs.annotations.command.MaxArgs;
import de.seine_eloquenz.lbcfs.annotations.command.MinArgs;
import de.seine_eloquenz.lbcfs.annotations.command.SubCommand;
import de.seine_eloquenz.lbcfs.command.LbcfsCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.annotation.command.Command;

/**
 * Shuts down the server with a countdown after 30s
 */
@MinArgs(0)
@MaxArgs(0)
@Command(name = "stop", desc = "Shuts down the server", permission = "lbcfs.admin")
public class CmdStop extends LbcfsCommand {

    private int i = 30;
    private int task;

    /**
     * Constructs a new {@link CmdStop} object
     * @param plugin the plugin the command belongs to
     */
    public CmdStop(final LbcfsPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean run(final CommandSender sender, final String[] args) {
        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.getPlugin(), () -> {
                    this.getPlugin().broadcast("Server wird in " + i + " Sekunden heruntergefahren.");
                    i -= 10;
                    if (i <= 10) {
                        Bukkit.getServer().getScheduler().cancelTask(task);
                        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.getPlugin(), () -> {
                                    this.getPlugin().broadcast(Integer.toString(i));
                                    --i;
                                    if (i < 1) {
                                        Bukkit.getServer().getScheduler().cancelTask(task);
                                        Bukkit.getServer().shutdown();
                                    }
                                }, 200L, 20L);
                    }
                }, 0L, 200L);
        return true;
    }

    /**
     * Specifies that the server shall be shut down immediately
     */
    @SubCommand(name = "now", parentCommand = CmdStop.class)
    public static final class Now extends de.seine_eloquenz.lbcfs.command.SubCommand {

        /**
         * Constructs a new {@link CmdStop} object
         * @param plugin the plugin the command belongs to
         */
        public Now(final LbcfsPlugin plugin) {
            super(plugin);
        }

        @Override
        public boolean run(final CommandSender sender, final String[] args) {
            Bukkit.getServer().shutdown();
            return true;
        }
    }
}
