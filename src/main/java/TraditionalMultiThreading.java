import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TraditionalMultiThreading {

    // Dummy API call
    static String getEmployeename(int id, Properties props) {
        System.out.println("Calling API for id = " + id + " on " + Thread.currentThread().getName());
        try {
            Thread.sleep(2000); // fake API delay
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

/* Here  we are using three platform threads only for 5 api calls and it is taking 4 seconds for it which
is better than single threaded execution.
*/
