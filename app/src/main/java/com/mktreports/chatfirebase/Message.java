package com.mktreports.chatfirebase;

public class Message {

    private String text;
    private long timesstamp;
    private String fromId;
    private String toId;



    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimesstamp() {
        return timesstamp;
    }

    public void setTimesstamp(long timesstamp) {
        this.timesstamp = timesstamp;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }
}