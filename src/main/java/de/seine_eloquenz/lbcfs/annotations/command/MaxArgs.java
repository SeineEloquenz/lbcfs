package de.seine_eloquenz.lbcfs.annotations.command;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies the maximum amount of parameters that can be supplied to a
 * {@link de.seine_eloquenz.lbcfs.command.LbcfsCommand} or a {@link de.seine_eloquenz.lbcfs.command.SubCommand}
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxArgs {

    /**
     * The maximum number of arguments that may be supplied
     * @return the maximum parameter count
     */
    int value();
}
