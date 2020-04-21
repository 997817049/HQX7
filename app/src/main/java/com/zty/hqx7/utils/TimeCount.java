package com.zty.hqx7.utils;


public class TimeCount {
    private long time;
    private static TimeCount timeCount;

    private TimeCount() {
    }

    public static TimeCount getInstance() {
        if (timeCount == null) {
            timeCount = new TimeCount();
        }
        return timeCount;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
