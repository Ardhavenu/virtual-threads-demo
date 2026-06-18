package com.multithreading.demo;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class VirtualThread {

    public static void main(String[] args) {
        Properties props = DemoUtils.loadProperties();
        Instant start = Instant.now();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<String>> futures = new ArrayList<>();

            for (int id = 1; id <= 5; id++) {
                final int employeeId = id;
                Future<String> future = executor.submit(() -> DemoUtils.getEmployeeNameForVirtualThread(employeeId, props));
                futures.add(future);
            }
            for (Future<String> future : futures) {
                DemoUtils.printFutureResult(future);
            }
        }

        Duration elapsed = Duration.between(start, Instant.now());
        System.out.println("Total time: " + elapsed.toMillis() + " ms or " + elapsed.toSeconds() + "sec");
    }
}

/* The important idea: virtual threads are useful for blocking I/O-style work, like API calls, DB calls, file calls, or your fake Thread.sleep().
When a virtual thread blocks, the Java runtime can free the underlying OS thread to do other work. Oracle says virtual threads are suitable for tasks that spend most of their time blocked,
but they are not intended for long-running CPU-intensive work
*/
