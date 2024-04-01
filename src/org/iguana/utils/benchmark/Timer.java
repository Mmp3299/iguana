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
        startSystemTime = getSystemTime();
        startUserTime = getUserTime();
    }

    public void stop() {
        endNanoTime = System.nanoTime();
        endSystemTime = getSystemTime();
        endUserTime = getUserTime();
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


    public static long getUserTime() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isCurrentThreadCpuTimeSupported() ? bean.getCurrentThreadUserTime() : 0L;
    }

    public static long getSystemTime() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isCurrentThreadCpuTimeSupported()
                ? (bean.getCurrentThreadCpuTime() - bean.getCurrentThreadUserTime())
                : 0L;
    }

}
