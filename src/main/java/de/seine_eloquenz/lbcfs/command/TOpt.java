package de.seine_eloquenz.lbcfs.command;

/**
 * Interface providing a method which returns a String array used in tab completion. Various pre-defined TOpts can be
 * obtained from {@link TOpts}
 */
@FunctionalInterface
public interface TOpt {

    /**
     * Gets the strings the {@link TOpt} represents
     * @return possibly empty string array
     */
    String[] get();
}
