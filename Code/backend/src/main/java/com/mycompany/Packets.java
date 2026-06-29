package com.company;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Packets extends Packet {

    private static final Gson GSON = new Gson();
    public static final char SEPARATOR = '|';

    // ── Mã Client → Server ──
    public static final String REGISTER = "REGISTER";
    public static final String LOGIN = "LOGIN";
    public static final String LOGOUT = "LOGOUT";
    public static final String PING = "PING";
    public static final String CREATE_ROOM = "CREATE_ROOM";
    public static final String JOIN_ROOM = "JOIN_ROOM";
    public static final String LEAVE_ROOM = "LEAVE_ROOM";
    public static final String MOVE = "MOVE";
    public static final String CHAT = "CHAT";

    // ── Mã Server → Client ──
    public static final String REGISTER_OK = "REGISTER_OK";
    public static final String REGISTER_FAIL = "REGISTER_FAIL";
    public static final String LOGIN_OK = "LOGIN_OK";
    public static final String LOGIN_FAIL = "LOGIN_FAIL";
    public static final String PONG = "PONG";
    public static final String ERROR = "ERROR";
    public static final String ROOM_LIST = "ROOM_LIST";
    public static final String ROOM_JOINED = "ROOM_JOINED";
    public static final String MOVE_OK = "MOVE_OK";
    public static final String MOVE_FAIL = "MOVE_FAIL";
    public static final String CHAT_MSG = "CHAT_MSG";

    private static String action = "";
    private static JsonObject payload = null;

    Packets(String action, JsonObject payload) {
        super();
        this.action = action;
        this.payload = payload != null ? payload : new JsonObject();
    }

    // ── Tạo Packet ──

    public static Packets of(String action, JsonObject payload) {
        return new Packets(action, payload);
    }

    public static Packets of(String action) {
        return new Packets(action, new JsonObject());
    }

    // ── Đóng gói: Packet → chuỗi TCP ──

    public static String encode() {
        return action + SEPARATOR + GSON.toJson(payload);
    }

    // ── Mở gói: chuỗi TCP → Packet ──

    public static Packets decode(String raw) {
        if (raw == null || raw.isBlank())
            throw new IllegalArgumentException("Gói tin rỗng.");

        int idx = raw.indexOf(SEPARATOR);
        if (idx < 0)
            throw new IllegalArgumentException(
                    "Không tìm thấy dấu '|' trong: " + raw);

        String action = raw.substring(0, idx).trim().toUpperCase();
        String jsonStr = raw.substring(idx + 1).trim();

        JsonObject payload;
        try {
            payload = JsonParser.parseString(jsonStr).getAsJsonObject();
        } catch (Exception e) {
            throw new IllegalArgumentException("JSON không hợp lệ: " + jsonStr);
        }

        return new Packets(action, payload);
    }

    // ── Truy cập dữ liệu ──

    public String getAction() {
        return action;
    }

    public JsonObject getPayload() {
        return payload;
    }

    public String get(String field) {
        if (payload.has(field) && !payload.get(field).isJsonNull())
            return payload.get(field).getAsString();
        return "";
    }

    public Object put(String field, String value) {
        payload.addProperty(field, value != null ? value : "");
        return this;
    }

    public Object put(String field, int value) {
        payload.addProperty(field, value);
        return this;
    }

    @Override
    public String toString() {
        return "Packet{action='" + action + "', payload=" + payload + "}";
    }

    // ── Factory methods tiện ích (FE dùng trực tiếp) ──

    public static Packets register(String username, String email, String password) {
        JsonObject d = new JsonObject();
        d.addProperty("username", username);
        d.addProperty("email", email != null ? email : "");
        d.addProperty("password", password);
        return of(REGISTER, d);
    }

    public static Packets login(String username, String password) {
        JsonObject d = new JsonObject();
        d.addProperty("username", username);
        d.addProperty("password", password);
        return of(LOGIN, d);
    }

    public static Packets ping() {
        return of(PING);
    }

    public static Packets loginOk(int userId, String username) {
        JsonObject d = new JsonObject();
        d.addProperty("userId", userId);
        d.addProperty("username", username);
        d.addProperty("message", "Đăng nhập thành công!");
        return of(LOGIN_OK, d);
    }

    public static Packets fail(String responseCode, String message) {
        JsonObject d = new JsonObject();
        d.addProperty("message", message);
        return of(responseCode, d);
    }
}




