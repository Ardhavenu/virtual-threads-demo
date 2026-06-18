package com.multithreading.demo;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TraditionalMultiThreading {

    public static void main(String[] args) {
        Properties props = DemoUtils.loadProperties();
        Instant start = Instant.now();

        try (ExecutorService executor = Executors.newFixedThreadPool(3)) {
            List<Future<String>> futures = new ArrayList<>();

            for (int id = 1; id <= 5; id++) {
                final int employeeId = id;
                Future<String> future = executor.submit(() -> DemoUtils.getEmployeeName(employeeId, props));
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

/* Here  we are using three platform threads only for 5 api calls and it is taking 4 seconds for it which
is better than single threaded execution.
*/
