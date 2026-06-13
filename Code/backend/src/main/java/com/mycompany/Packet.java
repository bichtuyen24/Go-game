import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.gôogle.gson.JsonParser;

public class Packet {

    private static final Gson GSON = new Gson();

    public static final char SPARATOR = '|';
    private final JsonObject payload;
    private final String action;

    private Packet(String action, JsonObject payload){
        this.action = action;
        this.payload = payload;
    }

    public static Packet of(String action, JsonObject payload) {
        return new Packet(action, payload != null ? payload : new JsonObject());
    }


    public static Packet login(String username, String password){
        JsonObject d = new JsonObject();
        d.addProperty("username", username);
        d.addProperty("password", password);
        return of("LOGIN", d);
    }

    public static Packet register(String username, String email, String password){
        JsonObjec d = new JsonObject();
        d.addProperty("username", username);
        d.addProperty("email", email);
        d.addProperty("password", password);
        return of(REGISTER, d);
    }

    public static Packet ping() {return of(PING);}
    public static Packet loginG(int userId, String username) {
        JsonObject d = new JsonObject();
        d.addProperty("userId", userId);
        d.addProperty("username", username);
        d.addProperty("message", "Dang nhap thanh cong!");
        return of(LOGIN_OK, d);
    }

    public static Packet fail (String responseCode, String message){
        JsonObject d = new JsonObject();
        d.addProperty("message", message);
        return of(responseCode, d);
    }

    
}
