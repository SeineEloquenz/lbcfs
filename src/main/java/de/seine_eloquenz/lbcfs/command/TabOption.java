package de.seine_eloquenz.lbcfs.command;

@FunctionalInterface
public interface TabOption {
    String[] of(String... elements);
}
