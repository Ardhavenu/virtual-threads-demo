package com.multithreading.demo;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class CompletableFutureAndASync {

    private static final Logger log = Logger.getLogger(CompletableFutureAndASync.class.getName());

    public static void main(String[] args) {
        Properties props = DemoUtils.loadProperties();
        Instant start = Instant.now();

        try (ExecutorService executor = Executors.newFixedThreadPool(3)) {
            List<CompletableFuture<Void>> futures = new ArrayList<>();

            for (int id = 1; id <= 5; id++) {
                final int employeeId = id;

                CompletableFuture<Void> future =
                        CompletableFuture.supplyAsync(() -> DemoUtils.getEmployeeName(employeeId, props), executor)
                                .thenAccept(System.out::println);
                futures.add(future);
            }
            try {
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            } catch (CompletionException e) {
                log.severe("Async task failed: " + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
            }
        }

        Duration elapsed = Duration.between(start, Instant.now());
        System.out.println("Total time: " + elapsed.toMillis() + " ms or " + elapsed.toSeconds() + "sec");
    }
}

/* Here, With using CompletableFuture and async methods , we can run asynchronously submit all tasks at once
same as traditional multithreading but it does not wait for the future 1 to arrive , it will print
any task's response based on not by blocking, whereas traditional approach always waits for future1
task to complete before printing other futures even if they complete before future 1.

But this wont affect much on the time saving but using completable future helps when dealing with mutliple tasks
and if we want to combine two tasks and we want to avoid manual and blocking code.
*/
