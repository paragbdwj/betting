package com.elephants.betting;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class JMXMetrics {
    public static void main(String[] args) {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();

        long maxHeapSize = heapMemoryUsage.getMax(); // Maximum heap size
        long initHeapSize = heapMemoryUsage.getInit(); // Initial heap size

        System.out.println("Initial Heap Size (Xms): " + (initHeapSize / (1024 * 1024)) + " MB");
        System.out.println("Maximum Heap Size (Xmx): " + (maxHeapSize / (1024 * 1024)) + " MB");
    }
}

