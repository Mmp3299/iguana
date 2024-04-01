package org.iguana.utils.benchmark;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class Timer {

    private long startNanoTime;
    private long startUserTime;
    private long startSystemTime;

    private long endNanoTime;
    private long endUserTime;
    private long endSystemTime;

    private boolean running;

    public void start() {
        if (running) {
            throw new RuntimeException("The timer is already running. Reset first before start!");
        }
        running = true;
        startNanoTime = System.nanoTime();
        startSystemTime = getCurrentSystemTime();
        startUserTime = getCurrentUserTime();
    }

    public void stop() {
        endNanoTime = System.nanoTime();
        endSystemTime = getCurrentSystemTime();
        endUserTime = getCurrentUserTime();
    }

    public void reset() {
        running = false;
        startNanoTime = 0;
        startUserTime = 0;
        startSystemTime = 0;
        endNanoTime = 0;
        endUserTime = 0;
        endSystemTime = 0;
    }

    public long getNanoTime() {
        return endNanoTime - startNanoTime;
    }

    public long getUserTime() { return endUserTime - startUserTime; }

    public long getSystemTime() { return endSystemTime - startSystemTime; }

    public static long getCurrentUserTime() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isCurrentThreadCpuTimeSupported() ? bean.getCurrentThreadUserTime() : 0L;
    }

    public static long getCurrentSystemTime() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isCurrentThreadCpuTimeSupported()
                ? (bean.getCurrentThreadCpuTime() - bean.getCurrentThreadUserTime())
                : 0L;
    }

}
