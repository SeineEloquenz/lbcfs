package de.seine_eloquenz.lbcfs.annotations.command;

import de.seine_eloquenz.lbcfs.command.LbcfsCommand;
import de.seine_eloquenz.lbcfs.command.SubCommand;
import org.bukkit.entity.Player;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation marks the {@link LbcfsCommand} or {@link SubCommand} it is used on to only be issued by an online
 * {@link Player}
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PlayerOnly {
}
