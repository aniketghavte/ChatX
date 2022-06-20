package com.projectbyaniket.chatx.ModelClass;

public class Massages {
    String massage;
    String senderId;
    long timeStamp;

    public Massages() {
    }

    public Massages(String massage, String senderId, long timeStamp) {
        this.massage = massage;
        this.senderId = senderId;
        this.timeStamp = timeStamp;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
