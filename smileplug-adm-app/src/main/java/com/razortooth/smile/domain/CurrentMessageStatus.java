package com.razortooth.smile.domain;

public enum CurrentMessageStatus {

    WAIT_CONNECT("Wait connect"),
    START_MAKE("Make Questions"),
    START_SOLVE("Solve Questions"),
    START_SHOW("Show Results");

    private String status;

    private CurrentMessageStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
