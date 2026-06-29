package com.mycompany;

ppublic class Protocol {
    public static final String LOGIN = "LOGIN";
    public static final String PING = "PING";

    // Quản lý phòng
    public static final String CREATE_ROOM = "CREATE_ROOM";
    public static final String JOIN_ROOM = "JOIN_ROOM";
    public static final String LEAVE_ROOM = "LEAVE_ROOM";
    public static final String ROOM_ERROR = "ROOM_ERROR";

    // Tương tác trong game
    public static final String CHAT = "CHAT";
    public static final String MOVE = "MOVE";
    public static final String PASS = "PASS";
    public static final String RESIGN = "RESIGN";
    public static final String GAME_ERROR = "GAME_ERROR";

    // Trung gian WebRTC Signaling
    public static final String RTC_OFFER = "RTC_OFFER";
    public static final String RTC_ANSWER = "RTC_ANSWER";
    public static final String RTC_ICE = "RTC_ICE";
}
