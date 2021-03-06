package de.seine_eloquenz.lbcfs.annotations.command;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies the name of a {@link de.seine_eloquenz.lbcfs.command.SubCommand}
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommand {

    /**
     * The name of the subCommand
     * @return name
     */
    String name();

    /**
     * The parent command this subcommand belongs to
     * @return the class of the parent
     */
    Class parentCommand();
}
