package com.simplejcode.commons.misc.struct;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class LimitManager {

    private final BigDecimal limit;

    private BigDecimal committed;

    private BigDecimal locked;


    public LimitManager(BigDecimal limit, BigDecimal committed, BigDecimal locked) {
        this.limit = limit;
        this.committed = committed;
        this.locked = locked;
    }


    public BigDecimal getRemained() {
        return limit.subtract(committed);
    }

    public BigDecimal getAvailable() {
        return limit.subtract(committed).subtract(locked);
    }


    public boolean canLock(BigDecimal lock) {
        BigDecimal availableLimit = limit.subtract(committed).subtract(locked);
        return lock.compareTo(availableLimit) <= 0;
    }

    public void lock(BigDecimal lock) {
        locked = locked.add(lock);
    }

    public void revertLock(BigDecimal lock) {
        locked = locked.subtract(lock);
    }

    public void commit(BigDecimal locked, BigDecimal processed) {
        this.locked = this.locked.subtract(locked);
        committed = committed.add(processed);
    }

    public void revertCommit(BigDecimal locked, BigDecimal processed) {
        this.locked = this.locked.add(locked);
        committed = committed.subtract(processed);
    }

}
