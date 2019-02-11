
import config.Configuration;
import data.Access;
import java.io.IOException;
import web.Router;

/**
 *
 * @author Walter Grönholm
 */
public class Main {

    private static final String DEFAULT_CONFIG_FILE_PATH = "config.ini";

    public static void main(String[] args) throws IOException {
        Configuration.initialize(args.length == 0 || args[0].isEmpty()
            ? DEFAULT_CONFIG_FILE_PATH
            : args[0]);
        Access.init(
            Configuration.databaseUrl(),
            Configuration.databaseUser(),
            Configuration.databasePass(),
            Configuration.databaseTimeout()
        );
        new Router().run();
    }
}
/*
 * To-do:
 * 1) Configuration file
 * 2) Logging: save to file
 * 3) Merge latest stock reports and display it
 * 4) Trend curves & extrapolation, stock estimation
 * 5) Calculate current (& estimated) stock values
 * 6) Automatic alerts when stock is running out
 * 
 * b1) Buy cash register or smth
 * 
 * Big change:
 * 10) Save data in SQLite(?) database (small, but must be able to handle 2 simulatenous users
 * 11) Consult Dan for data safety
 * 12) How to pay?
 * 13) Preparation for webaccounts
 * 
 */
