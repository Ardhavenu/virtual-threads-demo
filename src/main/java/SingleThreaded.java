import java.time.Instant;
import java.time.Duration;
import java.util.Properties;
import java.io.InputStream;

public class SingleThreaded {


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


    public static void main(String[] args) {
        Properties props = new Properties();
        try (InputStream input = SingleThreaded.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                props.load(input);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Instant start = Instant.now();

        for (int id = 1; id <= 5; id++) {
            System.out.println(getEmployeename(id,props));
        }

        Duration elapsed = Duration.between(start, Instant.now());
        System.out.println("Total time: " + elapsed.toMillis() + " ms or " + elapsed.toSeconds() + "sec");

    }
}


/* single main-thread running loop for 5 times getting employee names by ids.
* Each call takes 2 sec to respond , so we need to wait for 10 seconds for each call as its only main thread dealing with calls with
* no multi-threading.*/

