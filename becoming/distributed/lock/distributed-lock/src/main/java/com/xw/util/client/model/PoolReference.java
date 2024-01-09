package com.xw.util.client.model;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public enum PoolReference {
    INSTANCE;

    private final ConcurrentHashMap<Integer, AtomicInteger> poolRefs = new ConcurrentHashMap();

    private PoolReference() {
    }

    public void add(int poolHash) {
        if (this.poolRefs.containsKey(poolHash)) {
            ((AtomicInteger)this.poolRefs.get(poolHash)).incrementAndGet();
        } else {
            this.poolRefs.put(poolHash, new AtomicInteger(1));
        }

    }

    public int remove(int poolHash) {
        return this.poolRefs.containsKey(poolHash) ? ((AtomicInteger)this.poolRefs.get(poolHash)).decrementAndGet() : 0;
    }
}
