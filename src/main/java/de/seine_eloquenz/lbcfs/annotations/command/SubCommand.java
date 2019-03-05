package de.seine_eloquenz.lbcfs.annotations.command;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies a {@link de.seine_eloquenz.lbcfs.command.SubCommand} that belongs to a {@link de.seine_eloquenz.lbcfs.command.LbcfsCommand}
 */
@Documented
@Target(ElementType.TYPE)
@Repeatable(SubCommands.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommand {

    /**
     * The class of the {@link SubCommand}
     * @return the subCommand class
     */
    Class value();
}
