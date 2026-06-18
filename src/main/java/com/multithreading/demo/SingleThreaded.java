package com.multithreading.demo;

import java.time.Duration;
import java.time.Instant;
import java.util.Properties;

public class SingleThreaded {

    public static void main(String[] args) {
        Properties props = DemoUtils.loadProperties();
        Instant start = Instant.now();

        for (int id = 1; id <= 5; id++) {
            System.out.println(DemoUtils.getEmployeeName(id, props));
        }

        Duration elapsed = Duration.between(start, Instant.now());
        System.out.println("Total time: " + elapsed.toMillis() + " ms or " + elapsed.toSeconds() + "sec");
    }
}

/* single main-thread running loop for 5 times getting employee names by ids.
* Each call takes 2 sec to respond , so we need to wait for 10 seconds for each call as its only main thread dealing with calls with
* no multi-threading.*/
