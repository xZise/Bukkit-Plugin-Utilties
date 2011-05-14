package de.xzise.wrappers.economy;

import de.xzise.wrappers.Wrapper;

public interface EconomyWrapper extends Wrapper {

    AccountWrapper getAccount(String name);
    String format(int price);
    
}
