package json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author Walter Grönholm
 */
public class GsonFactory {

    public static Gson getGson() {
        return new GsonBuilder()
            .create();
    }
}
