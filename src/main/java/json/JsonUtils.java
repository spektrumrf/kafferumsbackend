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

    public static String jsonResponse(Object t, Type clazz, Response response) {
        response.type(APPLICATION_JSON);
        response.header("Access-Control-Allow-Origin", "*");
        response.header("Access-Control-Allow-Methods", "GET,POST,PUT");
        return getGson().toJson(t, clazz);
    }
    
    public static <T> String jsonResponse(T t, Class<T> clazz, Response response) {
        return jsonResponse(t, (Type) clazz, response);
    }
}
