package com.mycompany;

import java.io.Serializable;

public class Packet implements Serializable {
    private static final long serialVersionUID = 1L;

    private String type;
    private String sender;
    private String roomId;
    private String data;

    public Packet() {
    }

    public Packet(String type, String sender, String roomId, String data) {
        this.type = type;
        this.sender = sender;
        this.roomId = roomId;
        this.data = data;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
}