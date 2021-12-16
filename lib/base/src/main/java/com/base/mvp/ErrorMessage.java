package com.base.mvp;

public class ErrorMessage {

    public static final int TYPE_NETWORK_UNAVAILABLE = 100;

    private int type;

    public ErrorMessage() {
    }

    public ErrorMessage(int type) {
        this.type = type;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
