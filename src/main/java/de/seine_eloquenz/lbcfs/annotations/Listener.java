package de.seine_eloquenz.lbcfs.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares this class to be loaded as an EventListener by the corresponding {@link de.seine_eloquenz.lbcfs.LbcfsPlugin}
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Listener {
}
