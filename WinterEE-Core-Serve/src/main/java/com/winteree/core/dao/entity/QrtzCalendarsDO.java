package com.winteree.core.dao.entity;

public class QrtzCalendarsDO extends QrtzCalendarsDOKey {
    private byte[] calendar;

    public byte[] getCalendar() {
        return calendar;
    }

    public void setCalendar(byte[] calendar) {
        this.calendar = calendar;
    }
}