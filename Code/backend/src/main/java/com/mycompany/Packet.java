
import com.google.gson.JsonObject;

public class Packet {
    private final JsonObject payload;
    private final String action;

    private Packet(String action, JsonObject payload){
        this.action = action;
        this.payload = payload;
    }

    public static Packet of(String action, JsonObject payload) {
        return new Packet(action, payload != null ? payload : new JsonObject());
    }

    public static Packet fail (String responseCode, String message){
        JsonObject d = new JsonObject();
        d.addProperty("message", message);
        return of(responseCode, d);
    }
}
