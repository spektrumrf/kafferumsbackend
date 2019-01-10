package json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Type;
import spark.Response;

/**
 *
 * @author Walter Grönholm
 */
public class JsonUtils {

    private static final String APPLICATION_JSON = "application/json";

    public static Gson getGson() {
        return new GsonBuilder()
            .create();
    }

    public static <T> String jsonResponse(T t, Class<T> clazz, Response response) {
        response.type(APPLICATION_JSON);
        return getGson().toJson(t, clazz);
    }
}
