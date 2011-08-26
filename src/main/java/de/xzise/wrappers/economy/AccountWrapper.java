package de.xzise.wrappers.economy;

/**
 * A wrapper for accounts.
 * @author Fabian Neundorf
 */
public interface AccountWrapper {

    boolean hasEnough(double price);
    double getBalance();
    void add(double price);
    
}
