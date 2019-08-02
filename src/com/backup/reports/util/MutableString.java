package com.backup.reports.util;

/**
 * Created by ken on 7/24/2019.
 */
public class MutableString {
    private String myString;

    public MutableString(String myString) {
        this.myString = myString;
    }

    public MutableString() {
    }

    public String getMyString() {
        return myString;
    }

    public void setMyString(String myString) {
        this.myString = myString;
    }
}
