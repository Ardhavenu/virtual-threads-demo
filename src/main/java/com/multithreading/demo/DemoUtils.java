package com.multithreading.demo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public final class DemoUtils {

    private static final Logger log = Logger.getLogger(DemoUtils.class.getName());

    private DemoUtils() {
    }

    static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream in = DemoUtils.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            log.severe("Failed to load properties: " + e.getMessage());
        }
        return props;
    }

    static String getEmployeeName(int id, Properties props) {
        System.out.println("Calling API for id=" + id + " on " + Thread.currentThread().getName());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Employee name for id=" + id + " is " + props.getProperty("employee" + id + ".name");
    }

    static String getEmployeeNameForVirtualThread(int id, Properties props) {
        // toString() used intentionally — shows carrier thread assignment for virtual threads
        System.out.println("Calling API for id=" + id + " on " + Thread.currentThread().toString());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // toString() used intentionally — shows carrier thread assignment for virtual threads
        System.out.println("After Thread sleep, " + id + " on " + Thread.currentThread().toString());
        return "Employee name for id=" + id + " is " + props.getProperty("employee" + id + ".name");
    }

    static void printFutureResult(Future<String> future) {
        try {
            System.out.println(future.get());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted waiting for result", e);
        } catch (ExecutionException e) {
            log.severe("Task failed: " + e.getCause().getMessage());
        }
    }
}
