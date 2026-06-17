import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class VirtualThread {

    // Dummy API call
    static String getEmployeename(int id, Properties props) {
        System.out.println("Calling API for id = " + id + " on " + Thread.currentThread().toString());
        try {
            Thread.sleep(2000); // fake API delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("After Thread sleep ,  " + id + " on " + Thread.currentThread().toString());
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
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        ArrayList<Future<String>> futures = new ArrayList<>();

        for (int id = 1; id <= 5; id++) {
            final int employeeId = id;
            Future<String> future = executor.submit(() -> getEmployeename(employeeId,props));
            futures.add(future);
        }
        for (Future<String> future : futures) {
            try {
                System.out.println(future.get()); // waits for result
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Duration elapsed = Duration.between(start, Instant.now());
        System.out.println("Total time: " + elapsed.toMillis() + " ms or " + elapsed.toSeconds() + "sec");
        executor.shutdown();
    }
}

/* The important idea: virtual threads are useful for blocking I/O-style work, like API calls, DB calls, file calls, or your fake Thread.sleep().
When a virtual thread blocks, the Java runtime can free the underlying OS thread to do other work. Oracle says virtual threads are suitable for tasks that spend most of their time blocked,
but they are not intended for long-running CPU-intensive work
*/
