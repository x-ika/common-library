package com.simplejcode.commons.misc;

import com.simplejcode.commons.misc.util.ThreadUtils;

@Deprecated
public class MultiStateObject {

    private long[] endWait = new long[33];
    private int waiting;
    private int state;

    public synchronized boolean isWaitingFor(int event) {
        return (waiting & 1 << event) != 0;
    }

    public synchronized long timeRest(int event) {
        return endWait[event] - System.currentTimeMillis();
    }

    public synchronized void notify(int event) {
        if (isWaitingFor(event)) {
            state |= 1 << event;
            notify();
        }
    }

    public synchronized long setWaitFlags(int event, long timeout) {
        waiting |= 1 << event;
        return endWait[event] = System.currentTimeMillis() + timeout;
    }

    public synchronized void clear(int event) {
        state &= ~(1 << event);
        waiting &= ~(1 << event);
    }

    public synchronized void waitFor(int event, long timeout) {
        long end = setWaitFlags(event, timeout);
        while ((state & 1 << event) == 0) {
            try {
                wait(100);
                if (timeout != 0 && System.currentTimeMillis() > end) {
                    break;
                }
            } catch (InterruptedException ignore) {
            }
        }
        state &= ~(1 << event);
    }

    public synchronized void waitFor(int event) {
        waitFor(event, 0);
    }

    public synchronized void sleep(final long millis, final int slot) {
        new Thread() {
            public void run() {
                ThreadUtils.sleep(millis);
                MultiStateObject.this.notify(slot);
            }
        }.start();
        waitFor(slot);
        clear(slot);
    }

    public synchronized void sleep(long millis) {
        sleep(millis, 0);
    }

}