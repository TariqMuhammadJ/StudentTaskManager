package com.studytask.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TaskExecutor {
    private static final ExecutorService executor = Executors.newFixedThreadPool(5);

    public static void executeAsync(Runnable task) {
        executor.execute(task);
    }

    public static void shutdown() {
        try {
            executor.shutdown();
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
            // Close database connections
            ConnectionPool.closeAll();
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}