import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CompletableFutureAndASync {

    // Dummy API call
    static String getEmployeename(int id, Properties props) {
        System.out.println("Calling API for id = " + id + " on " + Thread.currentThread().getName());
        try {
            Thread.sleep(2000); // Intentional API delay for 2seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Employee name for id = " + id + " is " + props.getProperty("employee" + id + ".name");
    }

    public static void main(String args[]){
        Properties props = new Properties();
        try (InputStream input = SingleThreaded.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                props.load(input);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Instant start = Instant.now();
        ExecutorService executor = Executors.newFixedThreadPool(3);
        ArrayList<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int id = 1; id <= 5; id++) {
            final int employeeId = id;

            CompletableFuture<Void> future =
                    CompletableFuture.supplyAsync(() -> getEmployeename(employeeId, props), executor)
                            .thenAccept(System.out::println);
            futures.add(future);
        }
        // Important: wait for all async tasks to finish
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join(); // convert futures to Array and joining main thread.

        Duration elapsed = Duration.between(start, Instant.now());
        System.out.println("Total time: " + elapsed.toMillis() + " ms or " + elapsed.toSeconds() + "sec");
        executor.shutdown();
    }
}

/* Here, With using CompletableFuture and async methods , we can run asynchronously submit all tasks at once
same as traditional multithreading but it does not wait for the future 1 to arrive , it will print
any task's response based on not by blocking, whereas traditional approach always waits for future1
task to complete before printing other futures even if they complete before future 1.

But this wont affect much on the time saving but using completable future helps when dealing with mutliple tasks
and if we want to combine two tasks and we want to avoid manual and blocking code.
*/
