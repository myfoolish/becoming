package com.xw.util.client.inter;

public interface ISpecificPool {
    void shutdown();

    void shutdownNow();

    void execute(Runnable var1);

    void updatePool(String var1, String var2);
}
